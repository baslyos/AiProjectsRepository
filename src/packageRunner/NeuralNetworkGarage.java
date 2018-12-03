package packageRunner;

import java.util.List;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.Gaussian;

public class NeuralNetworkGarage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NeuralNetwork NN = NeuralNetwork.createFromFile(".\\Eclipse3NN1.nnet");
		Layer layer0 = NN.getLayerAt(0);
		List<Neuron> list0 = layer0.getNeurons();

		Layer layer1 = NN.getLayerAt(1);
		List<Neuron> list1 = layer1.getNeurons();
		Neuron source = list0.get(0);

		System.out.println(list0.get(0).getOutConnections().size()); // 27
		System.out.println(list1.get(25).getInputConnections().size()); // 26
		for (int i = 0; i <= 24; ++i) {
			list1.get(i).removeInputConnectionFrom(source);
		}
		for (int j = 1; j <= 25; ++j) {
			list1.get(25).removeInputConnectionFrom(list0.get(j));
			list1.get(26).removeInputConnectionFrom(list0.get(j));
			// list1.get(27).removeInputConnectionFrom(list0.get(j));
		}
		System.out.println(list1.get(25).getTransferFunction());
		// list1.get(25).setTransferFunction(new Linear());
		// list1.get(26).setTransferFunction(new Linear());
		// list1.get(27).setTransferFunction(new Linear());
		System.out.println(list1.get(25).getTransferFunction());
		System.out.println(list0.get(0).getOutConnections().size()); // 2
		System.out.println(list1.get(25).getInputConnections().size()); // 1
		Layer layer2 = NN.getLayerAt(2);
		List<Neuron> list2 = layer2.getNeurons();
		Layer layer3 = NN.getLayerAt(3);
		List<Neuron> list3 = layer3.getNeurons();
		for (Neuron nrn : list3) {
			nrn.setTransferFunction(new Gaussian());
			System.out.println("set");
		}
		for (Neuron nrn : list2) {
			nrn.setTransferFunction(new Gaussian());
			System.out.println("set");
		}
		for (Neuron nrn : list1) {
			nrn.setTransferFunction(new Gaussian());
			System.out.println("set");
		}

		// Layer l1 = NN.getLayerAt(1);
		// List<Neuron> li1 = l1.getNeurons();
		// // System.out.println(li1.size());
		// Neuron n0 = li0.get(2);
		// Neuron n1 = li1.get(1);
		// Neuron n2 = li1.get(2);
		// Neuron n3 = li1.get(3);
		// Neuron n4 = li1.get(4);
		// Neuron n5 = li1.get(5);
		// Neuron n6 = li1.get(6);
		// Neuron n7 = li1.get(7);
		// Neuron n8 = li1.get(8);
		// Neuron n10 = li1.get(0);
		// n2.removeInputConnectionFrom(n0);
		// n1.removeInputConnectionFrom(n0);
		// n3.removeInputConnectionFrom(n0);
		// n4.removeInputConnectionFrom(n0);
		// n5.removeInputConnectionFrom(n0);
		// n6.removeInputConnectionFrom(n0);
		// n7.removeInputConnectionFrom(n0);
		// n8.removeInputConnectionFrom(n0);
		// n10.removeInputConnectionFrom(n0);
		// n9.addInputConnection(n0, -0.8800);

		NN.save("Eclipse5NN2.nnet");
		System.out.println("done");
	}

}
