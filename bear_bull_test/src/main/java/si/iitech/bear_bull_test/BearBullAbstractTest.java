package si.iitech.bear_bull_test;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import si.iitech.test.AbstractTest;
import si.iitech.test.IExecute;

public class BearBullAbstractTest extends AbstractTest {
	
	@Transactional(value = TxType.REQUIRES_NEW)
	public void executeInTransaction(IExecute execute) {
		execute.execute();
	}
}
