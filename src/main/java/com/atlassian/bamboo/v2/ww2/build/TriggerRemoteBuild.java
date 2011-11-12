package com.atlassian.bamboo.v2.ww2.build;

import com.atlassian.bamboo.build.BuildDefinition;
import com.atlassian.bamboo.build.strategy.BuildStrategy;
import com.atlassian.bamboo.build.strategy.TriggeredBuildStrategy;
import com.atlassian.bamboo.logger.ErrorUpdateHandler;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.v2.events.ChangeDetectionRequiredEvent;
import com.atlassian.bamboo.ww2.actions.BuildActionSupport;
import com.atlassian.bamboo.ww2.aware.permissions.GlobalBypassSecurityAware;
import com.atlassian.event.EventManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * A Hacked version of the Atlassian version that does not check the source ip address (trigger ip is always diff from
 * the repo ip address), so I can trigger builds from git hub commits
 */
public class TriggerRemoteBuild extends BuildActionSupport implements GlobalBypassSecurityAware {
    private static final Logger log = Logger.getLogger(TriggerRemoteBuild.class);


    private EventManager eventManager;
    private ErrorUpdateHandler errorUpdateHandler;


    public void validate() {
        if (StringUtils.isEmpty(getPlanKey()) && StringUtils.isEmpty(getBuildKey())) {
            reportError("Build or Plan key not specified. Add request parameter of the form ?buildKey=BAM-BOO or ?planKey=BAM-BOO");
            return;
        }

        final Plan plan = getPlan();

        if (plan == null) {
            addActionError("Could not find the \"" + getBuildKey() + "\" plan");
            reportError("Build \"" + getBuildKey() + "\" is not known on this server");
            return;
        }

        BuildDefinition buildDefinition = plan.getBuildDefinition();

        BuildStrategy buildStrategy = buildDefinition.getBuildStrategy();
        if (!(buildStrategy instanceof TriggeredBuildStrategy)) {
            reportError("The plan \"" + plan.getName() + "\" is not configured for remote triggering. Strategy is " + (buildStrategy != null ? buildStrategy.getName() : "not set."));
        }
    }

    @Override
    public String doExecute() throws Exception {
        eventManager.publishEvent(new ChangeDetectionRequiredEvent(this, getPlan().getKey()));
        return SUCCESS;
    }

    /**
     * Log errors and provide them back to the user
     */
    private void reportError(String message) {
        addActionError(message);
        if (errorUpdateHandler != null && getBuild() != null) {
            errorUpdateHandler.recordError(getBuild().getKey(), "TriggerRemoteBuild : " + message);
        }
    }

    public void setEventManager(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setErrorUpdateHandler(final ErrorUpdateHandler errorUpdateHandler) {
        this.errorUpdateHandler = errorUpdateHandler;
    }
}
