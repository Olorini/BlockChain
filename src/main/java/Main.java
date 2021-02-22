import structure.BlockChain;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		BlockChain blockChain = new BlockChain();
		Scanner scanner = new Scanner(System.in);
		int zeroCount = Integer.parseInt(scanner.nextLine());
		blockChain.generateBlock(zeroCount);
		blockChain.save();
		System.out.println("---------------------------------------");
		System.out.println(blockChain);
		System.out.println("---------------------------------------");
		System.out.println(blockChain.validate());
	}
}
