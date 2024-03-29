package br.com.recatalog.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GitSourceManagementTest {
	
	
	public void testOKtoInstall() {
		
		assertTrue(true);
		
	}
	
	/*
	String repoPath;
	String fileToAddPath;
	PropertyList properties;
	
	@BeforeEach
	public void init() {
		repoPath =  "C:\\temp\\REPOSITORY";
		fileToAddPath =  "C:\\temp\\log.txt";
		properties = new PropertyList();
	}
	
	@Test
	public void testGitInit() {
		Repository repo = null;
		try {
			repo = GitSourceManagement.init(repoPath);
			repo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		assertNotNull(repo);
	}
	
	@Test
	  void testExpectedException() {
	    Assertions.assertThrows(IllegalStateException.class, () -> {
			Repository repo = null;
				repo = GitSourceManagement.init(repoPath);
				repo.close();
	    });
	  }
	
	@Test
	  void testGitStatus() {
		try {
			System.err.println(GitSourceManagement.gitStatus(repoPath));
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	  }
	
	@Test
	  void testAddAllFilesToStage() {
		try {
			GitSourceManagement.addAllFilesToStage(repoPath);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	  }
	
	@Test
	  void testIsCommitReady() {
		boolean ready = false;
		try {
			ready = GitSourceManagement.isCommitedReady(repoPath);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
		assertTrue(ready);
	  }
	
	@Test
	  void testUndo() {
		boolean ready = false;
		try {
			ready = GitSourceManagement.undoAll(repoPath);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
		assertTrue(ready);
	  }
	
	@Test
	  void testUnstageAll() {
		boolean ready = false;
		try {
			ready = GitSourceManagement.unStageAll(repoPath);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
		assertTrue(ready);
	  }
	
	@Test
	  void testIsStageReady() {
		boolean ready = false;
		try {
			ready = GitSourceManagement.isStageReady(repoPath);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
		assertTrue(ready);
	  }
	
	
	@Test
	  void testAddFilesToRepo() throws IOException {
		List<String> files = new ArrayList<String>();
		String baseFolder = "C:\\temp\\incluir";
		files.add("folder1\\logInfolder.txt");
		files.add("log.txt");
		
		List<File> filesAdded = GitSourceManagement.addFilesToRepoFolder(repoPath, files, baseFolder);
		assertTrue(filesAdded.size() != files.size());
	  }	
	  
	  */
	
	/*
	 * Verifica se repositorio não tem algo para "commitar"
	 */
	
	/*
	@Test
	  void testCommit() {
		properties.addProperty("AUTHOR", "Jose1");
		properties.addProperty("EMAIL", "jose.ze.fernando1@gmail.com");
		properties.addProperty("DESCRIPTION", "description1");
		try {
			RevCommit rev = GitSourceManagement.commit(repoPath, properties);
			assertTrue(rev != null);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	  }
	  */
	
	/*
	 * Verifica se repositorio não tem algo para "commitar"
	 */
	/*
	@Test
	  void testCommitNotReady() {
		properties.addProperty("AUTHOR", "Jose1");
		properties.addProperty("EMAIL", "jose.ze.fernando1@gmail.com");
		properties.addProperty("DESCRIPTION", "description1");
		try {
			RevCommit rev = GitSourceManagement.commit(repoPath, properties);
			assertTrue(rev == null);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
	  }
	  */
}