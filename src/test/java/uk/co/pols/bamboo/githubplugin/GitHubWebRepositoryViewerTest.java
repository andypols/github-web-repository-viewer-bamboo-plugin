package uk.co.pols.bamboo.githubplugin;

import com.atlassian.bamboo.plugins.git.GitRepository;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import org.jmock.integration.junit3.MockObjectTestCase;

import java.util.Arrays;

import static uk.co.pols.bamboo.githubplugin.GitHubWebRepositoryViewer.WEB_REPO_URL;
import static uk.co.pols.bamboo.githubplugin.SampleCommitFactory.commitWithFile;

public class GitHubWebRepositoryViewerTest extends MockObjectTestCase {
    private static final String WEB_REPO_FOR_THIS_PROJECT = "https://github.com/andypols/github-web-repository-viewer-bamboo-plugin";

    private GitHubWebRepositoryViewer gitHubViewer = new GitHubWebRepositoryViewer();

    public void testWorksWithTheAtlassianGitRepository() {
        assertEquals(Arrays.asList("com.atlassian.bamboo.plugins.atlassian-bamboo-plugin-git:git"), gitHubViewer.getSupportedRepositories());
    }

    public void testCanSetTheWebRepositoryUrlFromTheUI() {
        gitHubViewer.setWebRepositoryUrl(WEB_REPO_FOR_THIS_PROJECT);

        assertEquals(WEB_REPO_FOR_THIS_PROJECT, gitHubViewer.getWebRepositoryUrl());
    }

    public void testProvidesBambooWithWebUrlAllowingTheCodeChangePageLinkBackToGitHub() {
        gitHubViewer.setWebRepositoryUrl("https://github.com/andypols/git-bamboo-plugin");

        assertEquals("https://github.com/andypols/git-bamboo-plugin/commit/71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6", gitHubViewer.getWebRepositoryUrlForCommit(commitWithFile("71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6"), null));
//        assertEquals("https://github.com/andypols/git-bamboo-plugin/blob/71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6/src/main/java/uk/co/pols/bamboo/gitplugin/GitRepository.java", gitHubViewer.getWebRepositoryUrlForFile(SampleCommitFactory.commitFile("71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6")));
    }
}