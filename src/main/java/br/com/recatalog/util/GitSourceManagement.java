package br.com.recatalog.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitSourceManagement {
	
	//Erro ... java.lang.IllegalStateException: Repository already exists: C:\Git_Projects\SEGUROS\SINISTRO\.git
	public static Repository init(String  repoPath) throws IOException {
		File repoFolder = new File(repoPath);
		repoFolder.delete();
		repoFolder.mkdirs();
		
		Repository repo = FileRepositoryBuilder.create(new File(repoFolder, ".git"));
		repo.create();
		return repo;
	}
	
//	private static String exceptionToString(Exception e0) {
//		Path pathFile = null;
//		File tempFile = null;
//		String ret = null;
//		try {
//			pathFile = Files.createTempFile("arcatalogExceptionFormat", ".tmp");
//			tempFile = pathFile.toFile();
//			PrintStream err = new PrintStream(tempFile);
//			System.setErr(err);
//		} catch (Exception e) {
//			e.printStackTrace();
//			StringWriter exceptionStackError = new StringWriter();
//			e.printStackTrace(new PrintWriter(exceptionStackError));
//			ret =  exceptionStackError.toString();
//			System.setErr(System.err);
//			return ret;
//		}
//		e0.printStackTrace();
//		StringWriter exceptionStackError = new StringWriter();
//		e0.printStackTrace(new PrintWriter(exceptionStackError));
//		ret =  exceptionStackError.toString();
//		System.setErr(System.err);
//		return ret;
//	}
	
	public static void addFileToRepoAndStage(String repoPath, String fileToAddPath) throws IOException, NoFilepatternException, GitAPIException {
		String fileName = addFileToRepository(fileToAddPath, repoPath);
		addFileToStage(repoPath,fileName);
	}
	
	/*
	 * filePath pode ser um arquivo "arquivo.txt"
	 * ou pode ser um diretório, então todos os arquivos serão adicionados
	 * ou então pode ser "." onde todos os arquivos também serão adicionados
	 */
	
	public static void addFileToStage(String repoPath, String filePath) throws IOException, NoFilepatternException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		git.add().addFilepattern(filePath).call();
		git.close();
	}
	
	public static boolean addAllToStage(String repoPath) throws IOException, NoFilepatternException, GitAPIException {
		if(!hasModified(repoPath)) return false;
		Git git = Git.open(new File(repoPath));
		Set<String> files = getModifiedFiles(repoPath);
		AddCommand addCommand = git.add();
		for(String file : files) {
			addCommand.addFilepattern(file);
		}
		addCommand.call();
		git.close();
		return true;
	}
	
	public static void addFileByPattern(String repoPath, String filePath) throws IOException, NoFilepatternException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		git.add().addFilepattern(filePath).call();
		git.close();
	}
	
	public static boolean addx(String _newFile, String _repoFolder) throws IOException {
		Git git = Git.open(new File(_repoFolder));

		System.err.println(git.getRepository().getDirectory());
		try {
			String fileName = addFileToRepositoryx(_newFile, git.getRepository());
			if(fileName == null) return false;
			DirCache dc = git.add().addFilepattern(fileName).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
			return false;
		}
		return true;		
	}
	
	public static boolean remove(String _newFile, String _repoFolder) throws IOException {
		Git git = Git.open(new File(_repoFolder));
		try {
			DirCache index = git.rm().addFilepattern( _newFile ).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
			return false;
		}
		return true;		
	}

	public static RevCommit commit(String repoPath, PropertyList _properties) throws IOException, NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException, AbortedByHookException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		Status status = git.status().call();
		
//	    Set<String> uncommittedChanges = status.getUncommittedChanges();
//	    Set<String> modified = status.getModified();
//	    Set<String> added = status.getAdded();
	    Set<String> changed = status.getChanged();
//	    Set<String> removed = status.getRemoved();


//	    System.err.println("uncommittedChanges: " + uncommittedChanges);
//	    System.err.println("Modified: " + modified);
//	    System.err.println("added: " + added);
//	    System.err.println("changed: " + changed);
//	    System.err.println("removed: " + removed);

	    
	    // commit is needed?
	    if( changed.size() == 0) return null;
		
		RevCommit rev = git.commit().setAuthor((String)_properties.mustProperty("AUTHOR"), (String)_properties.mustProperty("EMAIL")).setMessage((String)_properties.mustProperty("DESCRIPTION")).call();	
		git.close();
		return rev;
	}
	
	public static Map<String, Set<String>> gitStatus(String repoPath) throws IOException, NoWorkTreeException, GitAPIException{
		Git git = Git.open(new File(repoPath));
		Status status = git.status().call();

		Map<String, Set<String>> stat = new HashMap<String,Set<String>>();

		stat.put("UNCOMMITED", status.getUncommittedChanges());
		stat.put("MODIFIED", status.getModified());
		stat.put("ADDED", status.getAdded());
		stat.put("CHANGED", status.getChanged());
		stat.put("REMOVED", status.getRemoved());
	    return stat;
	}
	
	public static boolean hasUncommited(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		Status status = git.status().call();
		return status.getUncommittedChanges().size() > 0;
	}
	
	/*
	 * desfaz as mudanças em todos os arquivos on working area
	 * git checkout -- .
	 */
	public static boolean undoAll(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		if(!hasModified(repoPath)) return false;
		Git git = Git.open(new File(repoPath));
		Set<String> files = getModifiedFiles(repoPath);
		CheckoutCommand checkout = git.checkout();
		for(String file : files) {
			checkout.addPath(file);
		}
		checkout.call();
		git.close();
		return true;
	}
	
	/*
	 * retorna os arquivos da stage area para a working area
	 * git reset HEAD .
	 */
	public static boolean unStageAll(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		if (!hasChanged(repoPath)) return false;
		Git git = Git.open(new File(repoPath));
		Set<String> files = getChangedFiles(repoPath);
		ResetCommand reset = git.reset();
		for(String file : files) {
			reset.addPath(file);
		}
		reset.call();
		git.close();
		return true;
	}
	
	public static Set<String> getModifiedFiles(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		Status status = git.status().call();
		return status.getModified();
	}
	
	public static Set<String> getChangedFiles(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		Status status = git.status().call();
		return status.getChanged();
	}
	
	public static boolean hasModified(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		Status status = git.status().call();
		return status.getModified().size() > 0;
	}	
	
	public static boolean hasAdded(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		Status status = git.status().call();
		return status.getAdded().size() > 0;
	}	
	
	public static boolean hasChanged(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		Git git = Git.open(new File(repoPath));
		Status status = git.status().call();
		return status.getChanged().size() > 0;
	}	
	
	public static boolean isCommitedReady(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		return hasChanged(repoPath);
	}
	
	public static boolean isStageReady(String repoPath) throws IOException, NoWorkTreeException, GitAPIException {
		return hasModified(repoPath);
	}
	
	public static boolean log(String _repoFolder) throws IOException {
		Git git = Git.open(new File(_repoFolder));

		System.err.println(git.getRepository().getDirectory());

		LogCommand logCmd =		git.log();

		try {
			for(RevCommit rv : logCmd.call()) {
				System.err.println(rv.toString());
			}
			System.err.println(logCmd.call().toString());
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;		
	}
	
	public static List<RevCommit> logs(String _repoFolder) throws IOException {
		Git git = Git.open(new File(_repoFolder));

		System.err.println(git.getRepository().getDirectory());

		LogCommand logCmd =	git.log();
		
		List<RevCommit> revCommits = new ArrayList<RevCommit>();
		
		try {
			for(RevCommit rv : logCmd.call()) {
				revCommits.add(rv);
//				System.err.println(rv.toString());
			}
//			System.err.println(logCmd.call().toString());
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return revCommits;		
	}
	
	public static String lastCommitId(String _repoFolder, boolean ...shortCommit) throws IOException {
		Git git = Git.open(new File(_repoFolder));
		Repository repo = git.getRepository();
		RevWalk walk = new RevWalk(repo);
		walk.markStart( walk.parseCommit( repo.resolve(Constants.HEAD)));
		String lastCommit = null;
		for( RevCommit rvCommit : walk ) {
			lastCommit = rvCommit.getId().toString();
			break;
		}
		walk.close();
		if(shortCommit.length > 0 &&  shortCommit[0] == true) {
			return lastCommit == null ? null :
				lastCommit.split(" ")[1].substring(0, 6);}
		else {
			return lastCommit == null ? null : lastCommit.split(" ")[1];}
	}
	
	public static String lastShortCommitId(String repoFolder) throws IOException {
		return lastCommitId(repoFolder, true);
	}
	
	public static PropertyList add(PropertyList props) {
		String repositoryDir = (String)props.getProperty("REPOSITORY_LOCATION");
		String addPattern = (String)props.getProperty("ADD_GIT_PATTERN_FILE");      // "."
		try {		
			Git git = Git.open(new File(repositoryDir));
			git.add().addFilepattern(addPattern).call();
		} catch (GitAPIException | IOException e) {
			StringWriter exceptionStackError = new StringWriter();
			e.printStackTrace(new PrintWriter(exceptionStackError));
			props = new PropertyList();
			props.addProperty("EXCEPTION", exceptionStackError.toString());
			return props;
		}
		return props;			
	}
	
	public static PropertyList commit(PropertyList props){
		String repositoryDir = (String)props.getProperty("REPOSITORY_LOCATION");
		try {
			Git git = Git.open(new File(repositoryDir));
			System.err.println(git.getRepository().getDirectory());
			RevCommit rev = git.commit().setAuthor((String)props.mustProperty("AUTHOR"), (String)props.mustProperty("EMAIL")).setMessage((String)props.mustProperty("DESCRIPTION")).call();	
			props.addProperty("REV_COMMIT", rev.getId().toString());
		} catch (GitAPIException | IOException e) {
			StringWriter exceptionStackError = new StringWriter();
			e.printStackTrace(new PrintWriter(exceptionStackError));
			props = new PropertyList();
			props.addProperty("EXCEPTION", exceptionStackError.toString());
			return props;
		}
		return props;		
	}
	
	private static String addFileToRepository(String _repositoryFile, String _repositoryFolder) throws IOException {
		File dir = (File) dirValidation(_repositoryFolder).getProperty("FILE");
		if(dir == null) return null;
		
		File file = (File) fileValidation(_repositoryFile).getProperty("FILE");
		if(file == null) return null;

		File gitFile = new File(dir,file.getName());
//		try {
			FileUtils.copyFile(file, gitFile);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
		return gitFile.getName();
	}
	
	private static String addFileToRepositoryx(String _repositoryFile, Repository _repository) {
		File file = new File(_repositoryFile);
		if(!file.isFile()) return null;

		File gitFile = new File(_repository.getDirectory().getParent(),file.getName());
		try {
			FileUtils.copyFile(file, gitFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return gitFile.getName();
	}
	
	private static PropertyList dirValidation(String _folder) {
		PropertyList ret = fileValidation(_folder);
		File dir = (File) ret.getProperty("FILE");
		
		if(dir == null) return ret;

		List<String> errorMsg = new ArrayList<String>();
		ret.addProperty("ERROR_MSG", errorMsg);
		
		if(!dir.isDirectory() || !dir.mkdir()) {
			errorMsg.add("INVALID DIRECTORY");
		}
		
		if(errorMsg.size() == 0) {
			ret.addProperty("FILE", dir);
		}

		return ret;
	}
	
	private static PropertyList fileValidation(String _file) {
		PropertyList ret = new PropertyList();
		File file = new File(_file);
		List<String> errorMsg = new ArrayList<String>();
		ret.addProperty("ERROR_MSG", errorMsg);
		
		if(!file.exists()) {
			errorMsg.add("FILE NOT FOUND");
		}
		
		if(errorMsg.size() == 0) {
			ret.addProperty("FILE", file);
		}
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
//		String gitFolder = "C:/repository_git";
		String gitFolder =  "C:\\Git_Projects\\SEGUROS\\R1PAB001";
//		boolean t = SCMGit.init(gitFolder);
//		System.out.println(t);
//		boolean v = add("*", gitFolder);
		
		System.err.println(lastCommitId(gitFolder));

///		boolean v = addPattern(".", gitFolder);
///		System.out.println(v);
		
/*		PropertyList properties = new PropertyList();
		
		properties.addProperty("AUTHOR", "José");
		properties.addProperty("EMAIL", "jose@gmail.com");
		properties.addProperty("DESCRIPTION", "Teste de commit.");
		
		boolean c = commit( gitFolder, properties);
		System.out.println(c);*/
		
//		boolean c = log(gitFolder);
	}
}