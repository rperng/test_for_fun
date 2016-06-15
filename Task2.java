package com.test.forfun;

public class Task2 {

	private static int[] test9 = {1,0,0,1,1};      // should return -9 {1,1,0,1}
	private static int[] test_6 = {0,1,1,1};       // should return  6 {0,1,0,1,1}
	private static int[] test_23 = {1,0,0,1,1,1};  // should return 23 {1,1,0,1,0,1,1}
	private static boolean DEBUG = false;  // true;
	
	public static int finalArray[];
	
	public static void main(String[] args) {
		Task2 ts = new Task2();
		
		// if we have the number but no digit_len ... need to try it one by one add up ..
		for (int i=250; i<260; i++) {
			int[] retIntArry = ts.findOppositeNumberFromDecValue(i);
			if (retIntArry!=null) {
				System.out.println("found in strDigits: " + retIntArry);
				printIntArry(retIntArry);
				System.out.println();
				System.out.println("valueOf is: " + ts.valueOf(retIntArry));
			} else {
				System.out.println("Got NULL value !");
			}
		}
		//ts.getDecValue(-6);		
		//ts.getDecValue(-9);		
		//ts.getDecValue(-23);
		
		int ret[] = ts.findOppositeNumber(test9);
		printIntArry(ret);
		System.out.print("\nvalue is: " + ts.valueOf(ret) + " for source: " + ts.valueOf(test9) + ": \n");	printIntArry(test9);
		System.out.println("\n");
		ret = ts.findOppositeNumber(test_6);
		printIntArry(ret);
		System.out.print("\nvalue is: " + ts.valueOf(ret) + " for source: " + ts.valueOf(test_6) + ": \n");	printIntArry(test_6);
		System.out.println("\n");
		ret = ts.findOppositeNumber(test_23);
		printIntArry(ret);
		System.out.print("\nvalue is: " + ts.valueOf(ret) + " for source: " + ts.valueOf(test_23) + ": \n");	printIntArry(test_23);
		System.out.println("\n");		
		// above is already know the digit_len of given number ...

	}
	
	public int[] guessTask2RepresentationFromDec(int decValue, int digits) {
		System.out.println("\nTrying to get: " + decValue + " with digits: " + digits);
		if (digits==0) {
			int[] retArryZero = {0};
			return retArryZero;
		}
		int M = digits;
    	int[] retArryM = new int[digits];

    	boolean retVal = tryFillFirstDigitB4RecursiveCall(retArryM, 1, decValue, M);    	
    	if (retVal) {
    		if (DEBUG) System.out.println("Found in M -1!");
    	} else {
    		retVal = tryFillFirstDigitB4RecursiveCall(retArryM, 0, decValue, M);   
        	if (retVal) {
        		if (DEBUG) System.out.println("Found in M -0!");
        	} else {
        		System.out.println("NO NO ...");
        		retArryM = null;
        	}
    	}
    	
    	return retArryM;
		
	}
	
	public int[] findOppositeNumberFromDecValue(int decValue) {
		int retVal[];	
		//
		String str = Integer.toBinaryString(Math.abs(decValue));
		int strDigits = str.length();
		// if it's > 0, strDigits should be odd number > strDigits+1
		// if it's < 0, strDigits should be even number > strDigits+1
		strDigits++;
		boolean odd = ( (strDigits % 2)==1 );
		if (decValue==0) {
			strDigits=0;
		}else if (decValue>0) {
			if (!odd) { strDigits++; }
		} else {  // <0
			if (odd) { strDigits++; }
		}
		
		retVal = guessTask2RepresentationFromDec(decValue, strDigits);		
		return retVal;
	}	

    public int[] findOppositeNumber(int[] A) {
    	int M = A.length;  //  M bits
    	int value = valueOf(A);   	
    	
    	// the opposite value will be in either M-1 or M+1 bits shown ...
    	int[] retArryM = new int[M-1];
    	boolean okInCall = tryFillFirstDigitB4RecursiveCall(retArryM, 1, -value, M-1);
    	if (okInCall) {
    		if (DEBUG) System.out.println("Found in M -1!");
    		return retArryM;
    	} else {
    		if (DEBUG) System.out.println("Found in M -0!");
    		okInCall = tryFillFirstDigitB4RecursiveCall(retArryM, 0, -value, M-1);
    		if (okInCall) {
    			return retArryM;
    		}
    	}
    	
    	retArryM = new int[M+1];
    	okInCall = tryFillFirstDigitB4RecursiveCall(retArryM, 1, -value, M+1);
    	if (okInCall) {
    		if (DEBUG) System.out.println("Found in M+1 -1!");
    		return retArryM;
    	} else {
    		okInCall = tryFillFirstDigitB4RecursiveCall(retArryM, 0, -value, M+1);
    		if (okInCall) {
    			if (DEBUG) System.out.println("Found in M+1 -0!");
    			return retArryM;
    		}
    	}
    	
    	return null;
    }
    
    public int valueOf(int[] A) {
    	int retVal = 0;
    	int singleDigitVal = 0;
    	for (int i=0; i<A.length; i++) {
    		// position on odd is minus ..
    		if ((i & 1)==0) {  // even position 
    			singleDigitVal = (int)Math.pow(2, i);
    			retVal += (A[i] * singleDigitVal);
    		} else {  // odd position
    			singleDigitVal = (int)Math.pow(2, i);
    			retVal -= (A[i] * singleDigitVal);
    		}
    	}  // for 
    	return retVal;
    }
    
    public static void printIntArry(int[] AA) {
    	if (AA==null) {
    		System.out.print("NULL!");
    	} else {
    		for (int i=0; i<AA.length; i++) {
    			System.out.print(AA[i] + ",");
    		}
    	}
    }
    
    
    private boolean tryFillFirstDigitB4RecursiveCall(int[] returnArray, int rightMostDigitVal, int X, int digitsLen) {
    	boolean retVal = false;
    	// if even-digits, it's negative
    	//boolean negative = (X < 0); //>( (digitsLen & 1)==0 ? true : false );
    	//int[] retIntArry = new int[digitsLen];
    	if (digitsLen<=0) {
    		return false;
    	}
    	// try all possible value of digitsLen to get value X
    	//int arryInDigitX[] = new int[digitsLen];  // createAOneInXDigit
    	//arryInDigitX[digitsLen-1]=1;   // need to retro back to here, might not be 1 here ...
    	int arryInDigitXFirstDigitOK[] = new int[digitsLen];
    	arryInDigitXFirstDigitOK[digitsLen-1]=rightMostDigitVal;
    	returnArray[digitsLen-1]=rightMostDigitVal;
    	int gotValueFirstDigitOK = valueOf(arryInDigitXFirstDigitOK);  	
    	
    	int remain = X - gotValueFirstDigitOK;
    	if (remain==0) {  // found the value
    		if (DEBUG) {
				System.out.print("-");    		
				printIntArry(returnArray); System.out.print("("+remain+")");
				printIntArry(arryInDigitXFirstDigitOK); System.out.print("("+remain+")");
				System.out.println();
    		}
    		// copy the dingle digit back to return Array ...
    		returnArray[digitsLen-1] = arryInDigitXFirstDigitOK[digitsLen-1];
    		retVal = true;
    	} else if (remain>0){  // need more positive number ... fewer than digitsLen, but next odd count ...
    		                                                   // 9->7, 5--> 3, 4--> 3, 3--> use 1, 2 --> use 1, 1 --> 0;
			if (DEBUG) {    		
				System.out.print("-");    		
				printIntArry(returnArray); System.out.print("("+remain+")");
				printIntArry(arryInDigitXFirstDigitOK); System.out.print("("+remain+")");
				System.out.println();
			}
    		int nextOddNumber = (digitsLen==1 ? 0 : ( ((digitsLen/2)*2)-1 ) );  // next odd number ...
    		boolean foundMatch = tryFillFirstDigitB4RecursiveCall(returnArray, 1, remain, nextOddNumber);
    		if (foundMatch) {
    			retVal = true;
    		} else {
    			foundMatch = tryFillFirstDigitB4RecursiveCall(returnArray, 0, remain, nextOddNumber);
    			if (foundMatch) {
        			retVal = true;    				
    			} else {		
    				if (DEBUG) System.out.println(" -*not here ... digitsLen: " + digitsLen + " remain: " + remain);
    			}
    		}
    	} else {  // (remain<0)  need more negative number ...fewer than digitsLen, but even count ...
    		                                                  // 3 --> use 2, 2 --> 0;
			if (DEBUG) {    		
				System.out.print("-");        		
				printIntArry(returnArray); System.out.print("("+remain+")");
				printIntArry(arryInDigitXFirstDigitOK); System.out.print("("+remain+")");
				System.out.println();
			}
    		int nextEvenNumber = (digitsLen==1 ? 0 : ( ((digitsLen-1)/2)*2) );  // next even number ...
    		boolean foundMatch = tryFillFirstDigitB4RecursiveCall(returnArray, 1, remain, nextEvenNumber);
    		if (foundMatch) {
    			retVal = true;
    		} else {
    			foundMatch = tryFillFirstDigitB4RecursiveCall(returnArray, 0, remain, nextEvenNumber);
    			if (foundMatch) {
        			retVal = true;    				
    			} else {
    				if (DEBUG) System.out.println(" -*not here 2 ... digitsLen: " + digitsLen + " remain: " + remain);
    			}
    		}    		
    	}
    	
    	return retVal;    	    	
    }

}  // Task2 ...
