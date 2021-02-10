import structure.BlockChain;

public class Main {

	public static void main(String[] args) {
		BlockChain blockChain = new BlockChain();
		blockChain.generateBlock();
		blockChain.generateBlock();
		blockChain.generateBlock();
		blockChain.generateBlock();
		blockChain.generateBlock();
		blockChain.generateBlock();
		blockChain.generateBlock();
		blockChain.generateBlock();
		blockChain.generateBlock();
		blockChain.generateBlock();
		System.out.println(blockChain);
		System.out.println(blockChain.validate());
	}
}
