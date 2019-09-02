package lzw;

import java.nio.ByteBuffer;

public class Testes {
	 public static void main (String arg[]) {
		 int k=1;
		 Testes conversor = new Testes();
		 long a = 254;
		 byte[] b = conversor.longToBytes(a);
		 byte[]c = conversor.parse(b,k); // o que deve ser inserido / será lido
		 byte[]d = conversor.preencher(c, k);
		 long r = conversor.bytesToLong(d);
		 
		 System.out.println("Long:" +a + "\n" + "Bytes:" + b +" - Bytes.lenght():"
		 +b.length +"\n"+ "Byte parseado: "+ c + " - ByteParseado.length(): " 
				 + c.length + "\nresultado: " + r );
		 
	 }
	 public byte[] longToBytes(long x) {
		    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		    buffer.putLong(x);
		    return buffer.array();
		}
	 
		public long bytesToLong(byte[] bytes) {
		    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		    buffer.put(bytes);
		    buffer.flip();//need flip 
		    return buffer.getLong();
		}
		
		public byte[] preencher(byte[]b, int k) {
			byte[] saida = new byte[8];
			int j=0;
				for(int i=0;i< saida.length;i++) {
					if(i <8-k ) {
						saida[i]= 00000000;
					}
					else {
						saida[i] = b[j];
						j++;
					}
				}
				return saida;
		}
		
		public byte[] parse(byte[] b, int k ) {
			byte[] saida = new byte[k];
			for(int i=0; i < k; i++) {
				saida[i] = b[8-k+i];
			}
			return saida;
		}
}