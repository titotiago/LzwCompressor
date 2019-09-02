package lzw;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LZW {
	StringBuffer v = new StringBuffer("");
	Testes conversor = new Testes();
	
	public void Encode(byte[] bytes, FileOutputStream fos, int k) throws IOException{
	    int temp=0;
	    Dicionario dic = new Dicionario();
	    dic.k = k;
	    String p = "";
	    String PC;
	    float i=1;
	    long l=0;
	    byte[] result;
	    for(byte b:bytes) {
	    char c = (char) (b & 0xFF);    
			PC = p + c;
			
			if (i%10000 == 0)
				System.out.println(i + "Codificando:" + (i/bytes.length)*100 + "%"); // Printar porcentagem do texto processado
	    	if (dic.codigo("" + PC ) != -1) {// se P + C estiver na lista
	        	p = PC;
	    	} else {
	    		
	    		temp = dic.codigo("" + p);
	    		l = (long) temp;
	    		result = conversor.longToBytes(l);
	    		byte[] a = conversor.parse(result,k);
	    		fos.write(a);
	    	
	    	//System.out.println(conversor.bytesToLong(result));
	        //	saida.append(temp + ""); // coloco a codificação na saida
	        	
	        	
	        	
	        	//Inserir PC no dicionario
	        	if(!dic.isFull)
	        		dic.inserir(PC);
	        	p = "" + c;
	    	}
	    	i++;
	    }
	    temp = dic.codigo("" + p);
	    l = (long) temp;
		result = conversor.longToBytes(l);
		byte[] a = conversor.parse(result,k);
		fos.write(a);

//    	saida.append(temp + "");
	}
	
	public static void compress(File source, File saida, int k) throws IOException{
		byte[] bytesArray = new byte[(int) source.length()]; //array de bytes do tamanho do arquivo
		FileInputStream fis = new FileInputStream(source);
		FileOutputStream fos = new FileOutputStream(saida);
		//FileOutputStream teste = new FileOutputStream(byteTeste);
		fis.read(bytesArray,0,(int)source.length()); // insiro em bytesArray todos os bytes do arquivo
		
		LZW alg = new LZW(); // instancio o algoritmo
		alg.Encode(bytesArray, fos, k);
		
	
		//byte[] strToBytes = alg.saida.toString().getBytes(); 
		
		//fos.write(strToBytes); // insiro o texto na saída
		fis.close();
		fos.close();
		//teste.close();

	}
	
	public String Decode(long l, Dicionario dic) {
	    String entry;
 	    if (v.length() == 0) {
	        v = new StringBuffer( dic.retornar((int)l) );
	        return(v.toString());
	    } else {
	        entry = new String( dic.retornar((int)l) );

	        if (entry.length() == 0) {          
	            entry = new String( v.toString() + v.charAt(0) );
	        }
	        if(!dic.isFull) {
	        	dic.inserir("" + v + entry.charAt(0) );
	        }
	        v = new StringBuffer(entry);
	        return entry;
	    }
	}
	
	public static void decompress(File source, File saida, int k) throws IOException{
		LZW alg = new LZW(); // instancio o algoritmo
		List<Long> codigos = new ArrayList<Long>();
		Dicionario dic = new Dicionario();		
		dic.k = k;
		Testes conversor = new Testes();
		FileOutputStream fos = new FileOutputStream(saida);
        byte[] bFile = readBytesFromFile("C:\\Users\\amori\\Desktop\\compressedK33.txt");
		byte[] buf = new byte[k];
		byte[] bufLong = new byte[8];
		long temp;
		StringBuffer result = new StringBuffer();
		
		// Trecho para pegar k Bytes por vez do array de bytes
		for (int i = 0; i < bFile.length; i++) {
			for(int j=0; j < k; j++)
				buf[j] = bFile[i+j];
			bufLong = conversor.preencher(buf, k);
			temp = conversor.bytesToLong(bufLong);
			codigos.add(temp);
        }
		
		// percorrer os bytes todos
		for(long l: codigos) {
			result.append(alg.Decode(l,dic));
		}
		byte[] strToBytes = result.toString().getBytes(); 

		fos.write(strToBytes);
		fos.close();
	}
	
	public static void main(String args[]) throws IOException{
		File source = new File("C:\\Users\\amori\\Desktop\\teste.txt");
		File comprimido = new File("C:\\Users\\amori\\Desktop\\compressed.txt");
		File descomprimido = new File("C:\\Users\\amori\\Desktop\\byteTeste.txt");
		int k=2;
		
		compress(source, comprimido, k);
		decompress(comprimido,descomprimido,k);
		
		
	}
	
	
	 private static byte[] readBytesFromFile(String filePath) {

	        FileInputStream fileInputStream = null;
	        byte[] bytesArray = null;

	        try {

	            File file = new File(filePath);
	            bytesArray = new byte[(int) file.length()];

	            //read file into bytes[]
	            fileInputStream = new FileInputStream(file);
	            fileInputStream.read(bytesArray);

	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (fileInputStream != null) {
	                try {
	                    fileInputStream.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }

	        }

	        return bytesArray;

	    }

	
}