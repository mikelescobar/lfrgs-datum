import java.util.Map;
import java.util.HashMap;
import com.liferay.custom.form.service.CustomFormServiceUtil;

Map<String, String> queryMap = new HashMap<String, String>();
queryMap.put("formInstanceId","123");
queryMap.put("screenName","test");
queryMap.put("name","test");
queryMap.put("type","test");

out.println(CustomFormServiceUtil.isSent(123, "test"));
out.println(CustomFormServiceUtil.send(queryMap).getMessage());
out.println(CustomFormServiceUtil.sign(123, "test").getSignature());
out.println(CustomFormServiceUtil.isSigned(123, "test"));
out.println(CustomFormServiceUtil.isSignedWorkflowAction("com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord", 36183, "approve", "incomplete"));