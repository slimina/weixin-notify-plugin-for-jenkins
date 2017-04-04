package cn.slimsmart.jenkins.plugin.weixinnotify;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.TaskListener;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import hudson.tasks.Notifier;
import hudson.util.FormValidation;

import java.io.IOException;

import javax.servlet.ServletException;

import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

//http://blog.csdn.net/feng_95271/article/details/12747337
public class WeixinNotifyBuilder extends Notifier implements SimpleBuildStep {

    private final String name;

    @DataBoundConstructor
    public WeixinNotifyBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void perform(Run<?,?> build, FilePath workspace, Launcher launcher, TaskListener listener) {
        
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
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        private boolean useFrench;

        public DescriptorImpl() {
            load();
        }

        
        public FormValidation doCheckName(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a name");
            if (value.length() < 4)
                return FormValidation.warning("Isn't the name too short?");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
        public String getDisplayName() {
            return "配置微信openid";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            useFrench = formData.getBoolean("useFrench");
            save();
            return super.configure(req,formData);
        }

        public boolean getUseFrench() {
            return useFrench;
        }
    }
}

