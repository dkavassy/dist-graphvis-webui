package graphvis.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ OutputParserTest.class, GMLTest.class, GraphMLTest.class, HDFSFileClientTest.class, ShellBuilderTest.class })
public class AllTests 
{
	
}
