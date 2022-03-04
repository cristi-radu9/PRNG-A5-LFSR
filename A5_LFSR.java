public class A5_LFSR {
	
	public static String getHex(byte[] array) {
		String output = "";
		for(byte value : array) {
			output += String.format("%02x", value);
		}
		return output;
	}
	
	public static void printNumbers(byte[] array) {
		System.out.printf("\nNumbers: \n");
		for( byte b : array) {
			System.out.printf("%d ",b);
		}
	}
	
	public static int majBit(int a,int b, int c) {
		if(a==b || a==c)
			return a;
		return b;
	}
	
	public static int shiftX(int[] x) {
		
		int bit= x[18] ^ x[17] ^ x[16] ^ x[13];
		int returnBit=x[x.length-1];
		int[] temp=new int[x.length];
		temp[0]=bit;
		for(int i=1;i<x.length;i++) {
			temp[i]=x[i-1];
		}
		
		for(int i=0;i<temp.length;i++) {
			x[i]=temp[i];
		}
		
		return returnBit;
	}
	
	public static int shiftY(int[] y) {
		
		int bit= y[21] ^ y[20];
		int returnBit=y[y.length-1];
		int[] temp=new int[y.length];
		temp[0]=bit;
		for(int i=1;i<y.length;i++) {
			temp[i]=y[i-1];
		}
		
		for(int i=0;i<temp.length;i++) {
			y[i]=temp[i];
		}
		
		return returnBit;
	}
	
	public static int shiftZ(int[] z) {
		
		int bit= z[22] ^ z[21] ^ z[20] ^ z[7];
		int returnBit=z[z.length-1];
		int[] temp=new int[z.length];
		temp[0]=bit;
		for(int i=1;i<z.length;i++) {
			temp[i]=z[i-1];
		}
		
		for(int i=0;i<temp.length;i++) {
			z[i]=temp[i];
		}
		
		return returnBit;
	}
	
	public static int XORBits(int[] x) {
		int returnBit=2;
		int cursor=0;
		
		for(int i=0;i<x.length;i++) {
			if(x[i]!=2) {
				returnBit=x[i];
				cursor=i+1;
				break;
			}
		}
		
		for(int i=cursor;i<x.length;i++) {
			if(x[i]!=2) {
				returnBit=returnBit^x[i];
			}
		}
		
		return returnBit;
	}
	
	public static void ByteArrayToInteger(int[] x,int[] y,int[] z,byte[] bytePassword) {
        String initialSeed = "";
        for(byte b:bytePassword) {
        	initialSeed = initialSeed + String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
        System.out.println("Initial seed: "+initialSeed);
        for(int i=0; i<19; i++)
            x[i] = Character.getNumericValue(initialSeed.charAt(i));

        for(int i=0; i<22; i++)
            y[i] = Character.getNumericValue(initialSeed.charAt(i+19));

        for(int i=0; i<23; i++)
            z[i] = Character.getNumericValue(initialSeed.charAt(i+41));
	}
	
	public static byte[] A5Generator(String password, int sequenceNoBytes) {
		
		byte[] bytePassword = password.getBytes();
		
		System.out.println("\nHex of password: "+getHex(bytePassword));
		
        int x[] = new int[19];
        int y[] = new int[22];
        int z[] = new int[23];
        
        ByteArrayToInteger(x,y,z,bytePassword);
        
        System.out.println("Register X: ");
        for(int i : x) {
        	System.out.printf("%d",i);
        }
        System.out.println("\nRegister Y: "); 
        for(int i : y) {
        	System.out.printf("%d",i);
        }
        System.out.println("\nRegister Z: ");
        for(int i : z) {
        	System.out.printf("%d",i);
        }
        System.out.printf("\n");
        
		int maj;
		int bitX,bitY,bitZ;
        
		int[] bitsToBeXORed= new int[3];

        for(int i=0;i<65;i++) {
    		maj=majBit(x[8], y[10], z[10]);
    		
    		for(int f=0;f<3;f++) {
    			bitsToBeXORed[f]=2;
    		}
    		
    		if(x[8]==maj) {
    			bitX=shiftX(x);
       			bitsToBeXORed[0]=bitX;
    		}
    		if(y[10]==maj) {
    			bitY=shiftY(y);
       			bitsToBeXORed[1]=bitY;
    		}
    		if(z[10]==maj) {
    			bitZ=shiftZ(z);
       			bitsToBeXORed[2]=bitZ;
    		}
    		
        }
        int repeat=sequenceNoBytes*8;
		byte[] resultBites=new byte[sequenceNoBytes];
		byte resultByte=0x00;
		int[] resultInts=new int[repeat];
		int cnt=7;
        for(int i=0;i<repeat;i++) {
    		maj=majBit(x[8], y[10], z[10]);
    		
    		for(int f=0;f<3;f++) {
    			bitsToBeXORed[f]=2;
    		}
    		
    		if(x[8]==maj) {
    			bitX=shiftX(x);
       			bitsToBeXORed[0]=bitX;
    		}
    		if(y[10]==maj) {
    			bitY=shiftY(y);
       			bitsToBeXORed[1]=bitY;
    		}
    		if(z[10]==maj) {
    			bitZ=shiftZ(z);
       			bitsToBeXORed[2]=bitZ;
    		}
    		
    		int resultBit= XORBits(bitsToBeXORed);
    		
    		resultInts[i]=resultBit;

    		if(i%8==0 && i!=0) {
    			cnt=7;
    			resultBites[(i/8)-1]=resultByte;
    			resultByte=(byte) (resultByte^resultByte);
    			if(resultBit!=0) {
        			resultByte=(byte) ((byte) (resultBit<<cnt) | resultByte);
    			}
    		}else {
    			if(resultBit!=0) {
        			resultByte=(byte) ((byte) (resultBit<<cnt) | resultByte);
    			}
    		}
			if(i==repeat-1) {
				++i;
    			cnt=7;
    			resultBites[(i/8)-1]=resultByte;
    			resultByte=(byte) (resultByte^resultByte);
    			if(resultBit!=0) {
        			resultByte=(byte) ((byte) (resultBit<<cnt) | resultByte);
    			}
			}
    		--cnt;
    		
        }
        
        System.out.printf("\nBits result:\n");
        for(int i : resultInts) {
        	System.out.printf("%d",i);
        }
        System.out.printf("\n\nHexa result:\n");
        System.out.println(getHex(resultBites));
		return resultBites;
	}

	public static void main(String[] args) {

		String password="fksndkfa";
		byte[] random = A5Generator(password, 8);
		printNumbers(random);
		byte[] random2 = A5Generator(password, 14);
		printNumbers(random2);
		
	}

}
