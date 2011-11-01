package uk.co.pols.bamboo.githubplugin;

import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.commit.CommitFile;
import org.jmock.integration.junit3.MockObjectTestCase;

import java.util.Arrays;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static uk.co.pols.bamboo.githubplugin.SampleCommitFactory.commitWithSingleFile;

public class GitHubWebRepositoryViewerTest extends MockObjectTestCase {
    private static final String WEB_REPO_FOR_THIS_PROJECT = "https://github.com/andypols/github-web-repository-viewer-bamboo-plugin";

    private GitHubWebRepositoryViewer gitHubViewer = new GitHubWebRepositoryViewer();
    private Commit commitWithFile = commitWithSingleFile("71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6", "author", "comment");
    private CommitFile committedFile = SampleCommitFactory.commitFile("71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6");


    /* Still supports old version of for people running on pre github repo */
    public void testWorksWithTheAtlassianGitRepository() {
        assertThat(gitHubViewer.getSupportedRepositories(), hasItem("com.atlassian.bamboo.plugins.atlassian-bamboo-plugin-git:git"));
    }

    public void testWorksWithTheAtlassianGitHubRepository() {
        assertThat(gitHubViewer.getSupportedRepositories(), hasItem("com.atlassian.bamboo.plugins.atlassian-bamboo-plugin-git:gh"));
    }

    public void testCanSetTheWebRepositoryUrlFromTheUI() {
        gitHubViewer.setWebRepositoryUrl(WEB_REPO_FOR_THIS_PROJECT);

        assertEquals(WEB_REPO_FOR_THIS_PROJECT, gitHubViewer.getWebRepositoryUrl());
    }

    public void testProvidesBambooWithToTheGitHubCommitPage() {
        gitHubViewer.setWebRepositoryUrl(WEB_REPO_FOR_THIS_PROJECT);

        assertEquals(WEB_REPO_FOR_THIS_PROJECT + "/commit/71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6", gitHubViewer.getWebRepositoryUrlForCommit(commitWithFile, null));
    }

    public void testProvidesBambooWithToTheGitHubCommitPageShowingTheFileDiff() {
        gitHubViewer.setWebRepositoryUrl(WEB_REPO_FOR_THIS_PROJECT);

        assertEquals(WEB_REPO_FOR_THIS_PROJECT + "/commit/71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6", gitHubViewer.getWebRepositoryUrlForDiff(committedFile, null));
    }

    public void testProvidesBambooWithToTheGitHubCommitPageShowingTheFile() {
        gitHubViewer.setWebRepositoryUrl(WEB_REPO_FOR_THIS_PROJECT);

        assertEquals(WEB_REPO_FOR_THIS_PROJECT + "/commit/71b2bf41fb82a12ca3d4d34bd62568d9167dc6d6", gitHubViewer.getWebRepositoryUrlForRevision(committedFile, null));
    }

    public void testProvidesBambooWithMapOfCommitsToUrls() {
        gitHubViewer.setWebRepositoryUrl(WEB_REPO_FOR_THIS_PROJECT);

        Commit commitOne = commitWithSingleFile("ONE", "author1", "comment");
        Commit commitTwo = commitWithSingleFile("TWO", "author2", "comment");

        Map<Commit,String> commitUrls = gitHubViewer.getWebRepositoryUrlForCommits(newArrayList(commitOne, commitTwo), null);

        assertEquals(WEB_REPO_FOR_THIS_PROJECT + "/commit/ONE", commitUrls.get(commitOne));
        assertEquals(WEB_REPO_FOR_THIS_PROJECT + "/commit/TWO", commitUrls.get(commitTwo));
    }
}