package uk.co.pols.bamboo.githubplugin;

import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.commit.CommitFile;
import com.atlassian.bamboo.repository.Repository;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.webrepository.CommitUrlProvider;
import com.atlassian.bamboo.webrepository.DefaultWebRepositoryViewer;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.opensymphony.util.UrlUtils;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GitHubWebRepositoryViewer extends DefaultWebRepositoryViewer implements CommitUrlProvider {
    public static final String REPO_PREFIX = "repository.githubplugin.";
    public static final String WEB_REPO_URL = REPO_PREFIX + "webRepositoryUrl";

    private String webRepositoryUrl;

    @Override
    public Collection<String> getSupportedRepositories() {
        return Arrays.asList("com.atlassian.bamboo.plugins.atlassian-bamboo-plugin-git:git");
    }

    @Override
    public void populateFromConfig(HierarchicalConfiguration config) {
        super.populateFromConfig(config);
        webRepositoryUrl = config.getString(WEB_REPO_URL);
    }

    @Override
    public HierarchicalConfiguration toConfiguration() {
        HierarchicalConfiguration configuration = super.toConfiguration();
        configuration.setProperty(WEB_REPO_URL, webRepositoryUrl);

        return configuration;
    }

    @Override
    public ErrorCollection validate(BuildConfiguration buildConfiguration) {
        ErrorCollection errorCollection = super.validate(buildConfiguration);

        String webRepoUrl = buildConfiguration.getString(WEB_REPO_URL);
        if (!StringUtils.isBlank(webRepoUrl) && !UrlUtils.verifyHierachicalURI(webRepoUrl)) {
            errorCollection.addError(WEB_REPO_URL, "This is not a valid url");
        }

        return errorCollection;
    }

    public void setWebRepositoryUrl(String url) {
        webRepositoryUrl = url;
    }

    public String getWebRepositoryUrl() {
        return webRepositoryUrl;
    }


//    public String getWebRepositoryUrlForFile(CommitFile commitFile) {
//        return webRepositoryUrl + "/blob/" + commitFile.getRevision() + "/" + commitFile.getName();
//    }

    public String getWebRepositoryUrlForCommit(Commit commit, Repository repository) {
        return webRepositoryUrl + "/commit/" + commitIdFor(commit);
    }

    private String commitIdFor(Commit commit) {
        List<CommitFile> files = commit.getFiles();
        if (files.isEmpty()) {
            return "UNKNOWN";
        }
        return files.get(0).getRevision();
    }
}