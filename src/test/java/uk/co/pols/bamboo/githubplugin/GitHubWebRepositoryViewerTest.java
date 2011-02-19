package uk.co.pols.bamboo.githubplugin;

import org.jmock.integration.junit3.MockObjectTestCase;

import java.util.Arrays;

public class GitHubWebRepositoryViewerTest extends MockObjectTestCase {
    private GitHubWebRepositoryViewer gitHubWebRepositoryViewer = new GitHubWebRepositoryViewer();

    public void testWorksWithTheAtlassianGitRepository() {
        assertEquals(Arrays.asList("com.atlassian.bamboo.plugins.atlassian-bamboo-plugin-git:git"), gitHubWebRepositoryViewer.getSupportedRepositories());
    }

//    public void testProvidesBambooWithWebUrlAllowingTheCodeChangePageLinkBackToGitHub() {
//        gitHubWebRepositoryViewer.setWebRepositoryUrl("https://github.com/andypols/git-bamboo-plugin");
//
//        assertEquals("https://github.com/andypols/git-bamboo-plugin/commit/71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6", gitHubWebRepositoryViewer.getWebRepositoryUrlForCommit(commitWithFile("71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6"), null));
//        assertEquals("https://github.com/andypols/git-bamboo-plugin/blob/71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6/src/main/java/uk/co/pols/bamboo/gitplugin/GitRepository.java", gitHubWebRepositoryViewer.getWebRepositoryUrlForFile(commitFile("71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6")));
//    }
}