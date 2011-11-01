package uk.co.pols.bamboo.githubplugin;

import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.commit.CommitFile;
import com.atlassian.bamboo.repository.RepositoryDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.webrepository.CommitUrlProvider;
import com.atlassian.bamboo.webrepository.DefaultWebRepositoryViewer;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.opensymphony.util.UrlUtils;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class GitHubWebRepositoryViewer extends DefaultWebRepositoryViewer implements CommitUrlProvider {
    public static final String REPO_PREFIX = "repository.githubplugin.";
    public static final String WEB_REPO_URL = REPO_PREFIX + "webRepositoryUrl";
    private static final String ATLASSIAN_GIT_PLUGIN_KEY = "com.atlassian.bamboo.plugins.atlassian-bamboo-plugin-git:git";
    private static final String ATLASSIAN_GITHUB_PLUGIN_KEY = "com.atlassian.bamboo.plugins.atlassian-bamboo-plugin-git:gh";

    private String webRepositoryUrl;

    @Override
    public Collection<String> getSupportedRepositories() {
        return Arrays.asList(ATLASSIAN_GIT_PLUGIN_KEY, ATLASSIAN_GITHUB_PLUGIN_KEY);
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

    @Override
    public String getWebRepositoryUrlForFile(CommitFile file, RepositoryDefinition repositoryDefinition) {
        return webRepositoryUrl + "/blob/" + file.getRevision() + "/" + file.getName();
    }

    @Override
    public String getWebRepositoryUrlForRevision(CommitFile file, RepositoryDefinition repositoryDefinition) {
        return githubCommitUrl(commitIdFor(file));
    }

    @Override
    public String getWebRepositoryUrlForDiff(CommitFile file, RepositoryDefinition repositoryDefinition) {
        return githubCommitUrl(commitIdFor(file));
    }

    @Override
    public Map<Commit, String> getWebRepositoryUrlForCommits(Collection<Commit> commits, RepositoryDefinition repositoryDefinition) {
        Map<Commit, String> results = new HashMap<Commit, String>();
        for (Commit commit : commits) {
            results.put(commit, githubCommitUrl(commitIdFor(commit)));
        }

        return results;
    }

    @Override
    public String getWebRepositoryUrlForCommit(Commit commit, RepositoryDefinition repositoryDefinition) {
        return githubCommitUrl(commitIdFor(commit));
    }

    private String commitIdFor(Commit commit) {
        List<CommitFile> files = commit.getFiles();
        if (files.isEmpty()) {
            return "UNKNOWN";
        }
        return commitIdFor(files.get(0));
    }

    private String commitIdFor(CommitFile file) {
        return file.getRevision();
    }

    private String githubCommitUrl(String commitId) {
        return webRepositoryUrl + "/commit/" + commitId;
    }
}