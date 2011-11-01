package uk.co.pols.bamboo.githubplugin;

import com.atlassian.bamboo.author.AuthorImpl;
import com.atlassian.bamboo.commit.CommitImpl;
import com.atlassian.bamboo.commit.CommitFileImpl;

import java.util.Date;

public class SampleCommitFactory {
    public static CommitImpl commitWithSingleFile(String revision, String author, String comment) {
        CommitImpl commit = new CommitImpl();
        commit.setAuthor(new AuthorImpl(author));
        commit.setDate(new Date());
        commit.setComment(comment);
        commit.addFile(commitFile(revision));
        return commit;
    }

    public static CommitFileImpl commitFile(String revision) {
        CommitFileImpl commitFile = new CommitFileImpl("src/main/java/uk/co/pols/bamboo/gitplugin/GitRepository.java");
        commitFile.setRevision(revision);
        return commitFile;
    }
}
