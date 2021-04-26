#!/bin/bash
set -e

#========================
## Commands
#========================
COMMAND="$1"

#========================
## Files
#========================
LICENSE_XML=activation/activation-key-digitalenterprisedevelopment-7.2-liferaycom.xml
LIFERAY_BUNDLE=bundles/liferay-dxp-tomcat-7.2.10.4-sp4-slim-20210302130725158.tar.gz
LIFERAY_BUNDLE_FOLDER_NAME=liferay-dxp-7.2.10.4-sp4
#LIFERAY_FIXPACK=fixpack/liferay-fix-pack-dxp-6-7210.zip
#LIFERAY_HOTFIX=hotfixes/liferay-hotfix-3056-7210.zip

#========================
## Variables
#========================
DATABASE="datum"
DATABASE_PASSWORD="root"
LIFERAY_HOME="liferay/bundles"
MAILCATCHER_NAME="datum-mail"
TOMCAT_VERSION="tomcat-9.0.40"

#========================
## Colors
#========================
blue=$'\e[1;34m'
cyan=$'\e[1;36m'
white=$'\e[0m'
red=$'\e[31m'

#========================
## Save and Load State
#========================
load_state() {
  log_info "Warning: Liferay container will be stopped!";
  log_debug read -p "Press any key to continue..." -n1 -s

  stop_server

  remove_docker_mysql $DATABASE
  create_docker_mysql $DATABASE
  
  load_data

  build_project
  start_server
}

save_state() {
  log_info "Warning: You will generate a dump!";
  log_debug read -p "Press any key to continue..." -n1 -s
  
  if [ ! -d "states" ]; then
    mkdir states
  else
    rm -rf states/*
  fi

  log_info "Info: Exporting document and library";
  if [ -d "$LIFERAY_HOME/data/document_library" ]; then
    cd $LIFERAY_HOME/data/document_library && zip -r ../../../../states/document_library.zip * && cd -
  fi

  log_info "Info: Creating database dump";
  docker exec $DATABASE sh -c 'exec mysqldump --all-databases -uroot --password="root"' > states/$DATABASE.sql
}

load_data() {
  if [ ! -d "$LIFERAY_HOME/data/document_library" ]; then
    log_debug mkdir -p $LIFERAY_HOME/data/document_library
  fi

  log_debug rm -rf $LIFERAY_HOME/data/document_library/*
  unzip -o states/document_library.zip -d $LIFERAY_HOME/data/document_library
  
  log_info "Restore database dump";
  
  cat states/$DATABASE.sql | docker exec -i $DATABASE mysql -u root --password=$DATABASE_PASSWORD $DATABASE
}

#========================
## SO
#========================
isWindows() {
  log_info "Detect OS";
  if [[ "$OSTYPE" == "msys" ]]; then
    log_info "Windows detected";
    return 0;
  else
    log_info "MacOS or Linux detected";
    return 1;
  fi
}

set_files_folder() {
  if isWindows; then
      FOLDER_PATH=files
    else
      FOLDER_PATH=~/.liferay
  fi  
  LICENSE_XML=$FOLDER_PATH/$LICENSE_XML
  LIFERAY_BUNDLE=$FOLDER_PATH/$LIFERAY_BUNDLE
  LIFERAY_FIXPACK=$FOLDER_PATH/$LIFERAY_FIXPACK
  LIFERAY_HOTFIX=$FOLDER_PATH/$LIFERAY_HOTFIX
}

#========================
## Mail Catcher
#========================
mail_catcher_up(){
  log_info "Creating mail catcher"

  log_debug docker run --name=$MAILCATCHER_NAME -d \
          --publish=1080:1080 \
          --publish=1025:1025 \
          dockage/mailcatcher:0.7.1
}

mail_catcher_stop(){
  log_info 'Stopping mailcatcher catcher'

  if docker ps -a | grep -q $MAILCATCHER_NAME; then
    log_debug docker stop $MAILCATCHER_NAME || true
    log_debug docker rm $MAILCATCHER_NAME || true
  fi 
}

#========================
## License
#========================
deploy_license() {
  log_info "Deploying Liferay license"
  if [ ! -d "$LIFERAY_HOME/deploy/" ]; then
    log_debug mkdir $LIFERAY_HOME/deploy/
  fi

  if [ -f $LICENSE_XML ]; then
    log_info "Deploying license file: \"$LICENSE_XML\""
    log_debug cp $LICENSE_XML $LIFERAY_HOME/deploy/
  else
    log_info "License not found at \"$LICENSE_XML\". Please deploy it manually"
  fi
}

#========================
## Patches
#========================
apply_patches(){
  log_info "Applying patches"
  cp $LIFERAY_FIXPACK $LIFERAY_HOME/patching-tool/patches
  cp $LIFERAY_HOTFIX $LIFERAY_HOME/patching-tool/patches
  ./$LIFERAY_HOME/patching-tool/patching-tool.sh install
  ./$LIFERAY_HOME/patching-tool/patching-tool.sh info
  log_debug rm -rf $LIFERAY_HOME/osgi/state
}

#========================
## Database
#========================
remove_docker_mysql() {
  log_info "Removing database docker container"
  if ! docker ps -a | grep -q CONTAINER
  then
    log_error "Docker does not seem to be running, run it first and retry"
    exit 1
  fi
  if docker ps -a -f name=$DATABASE | grep -q $DATABASE
  then
    log_debug docker stop $DATABASE
    log_debug docker rm $DATABASE
  fi  
}

create_docker_mysql() {
  log_info "Creating docker database container"
  if [ ! "$(docker ps -q -f name=$DATABASE)" ]; then
      log_info "Starting MySQL container...";
      log_debug docker run \
        --name $DATABASE \
        -p 3306:3306 \
        -e MYSQL_ROOT_PASSWORD=$DATABASE_PASSWORD \
        -e MYSQL_DATABASE=$DATABASE \
        -d mysql:8.0 \
        --character-set-server=utf8 \
        --collation-server=utf8_general_ci \
        --lower-case-table-names=0
      secs=$((40))
      while [ $secs -gt 0 ]; do
        echo -ne "$secs\033[0K\r"
        sleep 1
        secs=$((secs-1))
      done 
  fi
}

#========================
## Liferay set up
#========================
setup_bundle() {
  set_files_folder

  log_info "Setup bundle $LIFERAY_BUNDLE"
  touch $LIFERAY_HOME/.liferay-home

  log_debug tar -xzf $LIFERAY_BUNDLE -C $LIFERAY_HOME/
  log_debug mv $LIFERAY_HOME/$LIFERAY_BUNDLE_FOLDER_NAME/* $LIFERAY_HOME/
  log_debug rm -Rf $LIFERAY_HOME/$LIFERAY_BUNDLE_FOLDER_NAME

  log_info "Copying common and local files..."
  
  ## Copy OSGi Properties
  log_debug find liferay/configs/common -name \*.config -exec cp -rv {} $LIFERAY_HOME/osgi/configs \;
  
  ## Copy Portal Properties
  log_debug find liferay/configs/common -name \*.properties -exec cp -rv {} $LIFERAY_HOME \;
  log_debug find liferay/configs/local -name \*.properties -exec cp -rv {} $LIFERAY_HOME \;
  
  ## Copy Tomcat Files
  #log_debug find lcp/liferay/config/common/tomcat -exec cp -rv {} $LIFERAY_HOME/tomcat-9.0.17 \;
  log_debug cp -R liferay/configs/local/$TOMCAT_VERSION $LIFERAY_HOME
  log_debug mv $LIFERAY_HOME/portal-all.properties $LIFERAY_HOME/portal-bundle.properties
}

remove_bundle() {
  log_info "Removing bundle"
  read -p "This action will delete all the files inside ($LIFERAY_HOME) folder. Do you really want to proceed? [y/n]: " key
  if [[ $key = "y" ]]; then 
    stop_server
    mail_catcher_stop
    
    log_debug rm -rf $LIFERAY_HOME
    log_debug mkdir $LIFERAY_HOME
    log_debug rm -rf dump
    log_debug find . -name 'build' -type d -prune -exec rm -rf '{}' +
    log_debug find . -name 'node_modules' -type d -prune -exec rm -rf '{}' +
    log_debug find . -name 'dist' -type d -prune -exec rm -rf '{}' +
  else
    exit 1
  fi
}

clean_temps() {
  log_info "Cleaning temporary files..."
  log_debug rm -rf $LIFERAY_HOME/work $LIFERAY_HOME/osgi/state $LIFERAY_HOME/$TOMCAT_VERSION/work $LIFERAY_HOME/$TOMCAT_VERSION/temp

}

stop_server() {
  log_info "Stopping Liferay server"
  if [ -d "$LIFERAY_HOME" ]; then
    cd liferay
    blade server stop
    cd -
  fi
}

start_server() {
  log_info "Starting Liferay server"
  cd liferay
  blade server start -d
  cd -
  show_logs
}

build_project() {
  log_info "Building project"
  cd liferay/
  log_debug blade gw deploy
  cd -
}

#========================
## Debug
#========================
show_logs() {
  log_info "Liferay logs"
  while ! log_debug tail -f $LIFERAY_HOME/$TOMCAT_VERSION/logs/catalina.out ; do sleep 1 ; done
}

log_date() {
  echo $(date '+%Y-%m-%d %H:%M:%S') "$@"
}

log_debug() {
  log_date "DEBUG.: " $cyan "$@" $white
  "$@"
}

log_info() {
  echo "-------------------"
  log_date "INFO..: " $blue "$@" $white
  echo "-------------------"
}

log_error() {
  echo "-------------------"
  log_date "ERROR..: " $red "$@" $white
  echo "-------------------"
}

#========================
## Commands
#========================
init_command() {
  remove_bundle
  remove_docker_mysql
  create_docker_mysql
  setup_bundle
  load_data
  mail_catcher_up
  deploy_license
  #apply_patches
  clean_temps
  build_project
  start_server
}

#========================
## Interface
#========================
case "${COMMAND}" in
  init ) init_command
        exit 0
        ;; 
  start ) start_server
        exit 0
        ;;  
  stop ) stop_server
        exit 0
        ;;  
  clean ) clean_temps
        exit 0
        ;;  
  deploy ) build_project
        exit 0
        ;;  
  emailup ) mail_catcher_up
        exit 0
        ;;
  emailstop ) mail_catcher_stop
        exit 0
        ;;
  savestate ) save_state
        exit 0
        ;;
  loadstate ) load_state
        exit 0
        ;;      
   logs ) show_logs
        exit 0
        ;;               
  *)
    echo $"Usage:" "$0" "{init|start|stop|clean|deploy|emailup|emailstop|savestate|loadstate|logs}"
    exit 1
esac
exit 0