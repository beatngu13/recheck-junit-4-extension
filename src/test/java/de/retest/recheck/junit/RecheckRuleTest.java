package de.retest.recheck.junit;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.retest.recheck.RecheckLifecycle;

@RunWith( MockitoJUnitRunner.class )
public class RecheckRuleTest {

	public class DummyTest {

		private Object nonRecheck;
		private RecheckLifecycle recheck;
		private RecheckLifecycle otherRecheck;
	}

	@Mock
	private Statement base;
	private DummyTest testInstance;
	private Statement statement;
	private Description description;

	@Before
	public void before() {
		testInstance = new DummyTest();
		testInstance.recheck = mock( RecheckLifecycle.class );
		testInstance.otherRecheck = mock( RecheckLifecycle.class );
		testInstance.nonRecheck = mock( Object.class );
		description = Description.createTestDescription( testInstance.getClass(), "dummy-instance" );
		statement = new RecheckRule( testInstance ).apply( base, description );
	}

	@Test
	public void callsLifecycleMethods() throws Throwable {
		final InOrder inOrder = inOrder( testInstance.recheck, testInstance.otherRecheck, base );

		statement.evaluate();

		inOrder.verify( testInstance.recheck ).startTest( description.getDisplayName() );
		inOrder.verify( testInstance.otherRecheck ).startTest( description.getDisplayName() );
		inOrder.verify( base ).evaluate();
		inOrder.verify( testInstance.recheck ).capTest();
		inOrder.verify( testInstance.otherRecheck ).capTest();
	}

	@After
	public void after() {
		verifyZeroInteractions( testInstance.nonRecheck );
	}

}
