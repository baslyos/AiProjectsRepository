package packageRunner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class TrainingDataGarage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = null;
		try {
			in = new Scanner(Paths.get("pzRnrTestDataSet1"), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// int counter = 0;
		ArrayList<String> inputs = new ArrayList<>(5000);
		ArrayList<String> outputs = new ArrayList<>(5000);
		String tempString = "";
		int lastCommaIndex = 0;
		int counterx = 0;
		int counterxx = 0;

		while (in.hasNext()) {
			tempString = in.nextLine();
			// System.out.println(tempString);
			++counterx;
			lastCommaIndex = tempString.lastIndexOf(',');
			inputs.add(tempString.substring(0, lastCommaIndex));
			outputs.add(tempString.substring(lastCommaIndex + 1));
			// ++counter;

			// if (counter == 40)
			// break;
		}
		int zeros, negOnes, posOnes;
		for (int i = 0; i < inputs.size(); ++i) {
			if (inputs.get(i).equals(""))
				continue;
			zeros = 0;
			negOnes = 0;
			posOnes = 0;
			if (outputs.get(i).equals("-1.0"))
				++negOnes;
			else if (outputs.get(i).equals("0.0"))
				++zeros;
			else if (outputs.get(i).equals("1.0"))
				++posOnes;
			for (int j = i + 1; j < inputs.size(); ++j) {
				if (inputs.get(i).equals(inputs.get(j))) {
					inputs.set(j, "");
					if (outputs.get(j).equals("-1.0"))
						++negOnes;
					else if (outputs.get(j).equals("0.0"))
						++zeros;
					else if (outputs.get(j).equals("1.0"))
						++posOnes;
				}
			}
			if (negOnes >= (2 * (zeros + posOnes)))
				outputs.set(i, "-1.0");
			else if (zeros >= (2 * (negOnes + posOnes)))
				outputs.set(i, "0.0");
			else if (posOnes >= (2 * (negOnes + zeros)))
				outputs.set(i, "1.0");
			else {
				inputs.set(i, "");
				System.out.println(negOnes + " " + zeros + " " + posOnes);
			}
		}

		System.out.println(inputs.size() + " " + outputs.size());
		// int counter2 = 0;
		DataSet ds = new DataSet(26, 1);
		String[] tempStringArray = null;
		double[] temp26 = null;
		double[] temp1 = null;
		int LL = -1;
		while (++LL < inputs.size()) {
			if (inputs.get(LL).equals("")) {
				continue;
			}
			tempStringArray = inputs.get(LL).split(",");
			temp26 = new double[26];
			temp1 = new double[1];
			temp1[0] = Double.parseDouble(outputs.get(LL));
			// System.out.println(inputs.get(counter2));
			for (int i = 0; i < tempStringArray.length; ++i) {
				temp26[i] = Double.parseDouble(tempStringArray[i]);
			}
			ds.addRow(new DataSetRow(temp26, temp1));
			++counterxx;
			// System.out.println();
			// ++counter2;
		}
		ds.saveAsTxt(".\\pzRnrOptimizedDataSet", ",");
		System.out.println("\n\n\n\n" + counterx + "  " + counterxx);
	}

}
