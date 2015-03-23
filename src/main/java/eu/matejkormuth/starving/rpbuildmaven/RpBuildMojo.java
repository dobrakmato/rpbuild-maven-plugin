package eu.matejkormuth.starving.rpbuildmaven;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import eu.matejkormuth.rpbuild.Assembler;
import eu.matejkormuth.rpbuild.api.Project;
import eu.matejkormuth.rpbuild.configuration.xml.XmlBuildStepCompile;
import eu.matejkormuth.rpbuild.configuration.xml.XmlBuildStepGenerate;
import eu.matejkormuth.rpbuild.configuration.xml.XmlProject;

@Mojo(name = "rpbuild")
public class RpBuildMojo extends AbstractMojo {
	@Parameter
	private File configurationFile;

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!configurationFile.exists()) {
			throw new MojoFailureException(
					"file speciied in parameter configurationFile does not exists!");
		}

		try {
			JAXBContext context = JAXBContext.newInstance(XmlProject.class,
					XmlBuildStepCompile.class, XmlBuildStepGenerate.class);
			Object projectObj = context.createUnmarshaller().unmarshal(
					configurationFile);
			runBuild((Project) projectObj);
		} catch (JAXBException e) {
			throw new MojoExecutionException(
					"Deserialization of configuration file failed!", e);
		}
	}

	private void runBuild(Project project) {
		Assembler assembler = new Assembler(project);
		assembler.build();
	}
}
