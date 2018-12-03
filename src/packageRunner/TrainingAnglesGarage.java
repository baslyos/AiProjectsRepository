package packageRunner;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class TrainingAnglesGarage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataSet ds = new DataSet(1, 1);
		for (int i = 1; i <= 90; ++i) {
			// System.out.println((i - 45D) / 45D);
			ds.addRow(new DataSetRow(new double[] { i }, new double[] { i / 90D }));
			// System.out.println(ds.getRowAt(i - 1));
		}
		ds.saveAsTxt(".\\SigmoidOutputDataSet", ",");
	}

}
