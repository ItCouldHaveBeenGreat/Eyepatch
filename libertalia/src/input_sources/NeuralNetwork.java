package input_sources;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

public class NeuralNetwork {

	public void createNetwork() {
		try(Graph g = new Graph()) {
			g.opBuilder("blah", "bloo").build();
			try (Session s = new Session(g)) {
				List<Integer> inputList = new ArrayList<>();
				inputList.add(1);
				Tensor input = Tensor.create(inputList.toArray());
				Tensor output = s.runner()
					.feed("dnn/input_from_feature_columns/input_from_feature_columns/concat", input)
					.fetch("dnn/binary_logistic_head/predictions/probabilities").run().get(0);
				System.out.println(output);
			}
		}
	}
	
	public void test() {
	    try (Graph g = new Graph()) {
	    	final String value = "Hello from " + TensorFlow.version();

		     // Construct the computation graph with a single operation, a constant
		     // named "MyConst" with a value "value".
		     try (Tensor t = Tensor.create(value.getBytes("UTF-8"))) {
		        // The Java API doesn't yet include convenience functions for adding operations.
		        g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
		     } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		     // Execute the "MyConst" operation in a Session.
		     try (Session s = new Session(g); Tensor output = s.runner().fetch("MyConst").run().get(0)) {
		    	 System.out.println(new String(output.bytesValue(), "UTF-8"));
		     } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	public static void main(String args[]) {
		NeuralNetwork nn = new NeuralNetwork();
		nn.createNetwork();
	}
}

