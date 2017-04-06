package cn.slimsmart.jenkins.plugin.weixinnotify;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStep;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;

//http://blog.csdn.net/feng_95271/article/details/12747337
//http://www.360doc.com/content/15/1015/11/7811581_505778742.shtml
public class WeixinNotifyBuilder extends Notifier implements BuildStep{
 
	private final Boolean isSend;

    @DataBoundConstructor
    public WeixinNotifyBuilder(Boolean isSend) {
    	super();
    	this.isSend = isSend;
    }
	public Boolean getIsSend() {
		return isSend;
	}

    @Override
	public BuildStepMonitor getRequiredMonitorService() {
		return null;
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public BuildStepDescriptor getDescriptor() {
        return super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
    	
    	private String serverIp ="127.0.0.1";
        private Integer serverPort = 80;
        private String apKey;
        private String secretKey;
        private String templateId;
        
        private static final Pattern IP_PATTERN=Pattern.compile("/^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)($|(?!\\.$)\\.)){4}$/");

        public DescriptorImpl() {
            load();
        }

        public FormValidation doCheckName(@QueryParameter("serverIp") String serverIp,
        		@QueryParameter("serverPort") String serverPort,
        		@QueryParameter("apKey") String apKey,
        		@QueryParameter("secretKey") String secretKey,
        		@QueryParameter("templateId") String templateId)
                throws IOException, ServletException {
        	if(serverIp==null || (serverIp=serverIp.trim()).length()==0){
        		 return FormValidation.error("请输入Weixin Open API Server IP");
        	}
        	if(!IP_PATTERN.matcher(serverIp).find()){
        		return FormValidation.error("Weixin Open API Server IP输入不合法");
        	}
            if (serverPort==null || (serverPort=serverPort.trim()).length()==0){
            	 return FormValidation.error("请输入Weixin Open API Server Port");
            }
            int port = 0;
            try {
            	port = Integer.valueOf(serverPort);
			}catch(Exception e){}
            if(port<=0 || port >= 65535){
            	return FormValidation.error("Weixin Open API Server Port输入不合法");
            }
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
        public String getDisplayName() {
            return "微信通知";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        	serverIp = formData.getString("serverIp");
        	serverPort = formData.getInt("serverPort");
        	apKey = formData.getString("apKey");
        	secretKey = formData.getString("secretKey");
        	templateId = formData.getString("templateId");
            save();
            return super.configure(req,formData);
        }

		public String getServerIp() {
			return serverIp;
		}

		public Integer getServerPort() {
			return serverPort;
		}

		public String getApKey() {
			return apKey;
		}

		public String getSecretKey() {
			return secretKey;
		}

		public String getTemplateId() {
			return templateId;
		}
    }
}

