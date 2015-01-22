package calculateSystem;

import java.io.*;
import java.util.*;

public abstract class Definition {
	public Definition(){}
	public Definition(String code, String defFileName){
		this.code = code;
		this.defFileName = defFileName;
	}
	private String code;
	private String defFileName;
	private long sales;
	public String getCode(){
		return code;
	}
	public String getDefFileName(){
		return defFileName;
	}
	public long getSales(){
		return sales;
	}
	/**
	 * 
	 * @param sales
	 */
	public void calculate(long sales){
		this.sales += sales;
	}
	/**
	 * 
	 * @param path
	 * @param defFileName
	 * @return
	 * @throws IOException
	 */
	List<Definition> readDefinitionFile(String path, String defFileName) throws IOException{
		List<Definition> list = new ArrayList<Definition>();
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(path+"/"+defFileName+".lst"));
			String line;
			String[] lineList;
			while((line=br.readLine())!=null){
				lineList = line.split(",");
				for(int i=0;i<lineList.length;i++){
					if(!checkFormat(lineList[i], i)){
						System.err.println(defFileName+"定義フォーマットが不正です。\n処理を終了します。");
						System.exit(1);
					}
				}
				Definition def = new Branch(lineList[0], lineList[1]);
				list.add(def);
			}
		}catch(FileNotFoundException e){
			System.err.println("定義ファイルがありません。\n処理を終了します。");
			System.exit(1);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}finally{
			br.close();
		}
		return list;
	}
	abstract boolean checkFormat(String format, int count);
	 
	// やっていること同じだからもう一回forを回したほうがいい？
	//abstract void calculateSales(List<Definition> defFileName, List<Sales> readSales);
	
	static void calculateSales(List<Definition> branch, List<Definition> commodity, List<Sales> readSales){
		long MAX_RANGE = 1000000000L;
		for(int i=0;i<readSales.size();i++){
			int branchFlg = 0;
			int branchSuffix = -1;
			for(int j=0;j<branch.size();j++){
				if(branch.get(j).getCode().equals(readSales.get(i).getBranchCode())){
					branchFlg = 1;
					branchSuffix = j;
				}
			}
			int commodityFlg = 0;
			int commoditySuffix = -1;
			for(int j=0;j<commodity.size();j++){
				if(commodity.get(j).getCode().equals(readSales.get(i).getCommodityCode())){
					commodityFlg = 1;
					commoditySuffix = j;
				}
			}
			if(branchFlg==1 && commodityFlg==1){
				branch.get(branchSuffix).calculate(readSales.get(i).getSales());
				commodity.get(commoditySuffix).calculate(readSales.get(i).getSales());
				if(MAX_RANGE<=branch.get(branchSuffix).getSales() ||
						MAX_RANGE<=commodity.get(commoditySuffix).getSales()){
					System.err.println("合計金額が10桁を越えました。処理を終了します。");
					System.exit(1);
				}
			}else if(branchFlg==0){
				System.err.println(readSales.get(i).getFileName()+"の支店コードが不正です。");
			}else if(commodityFlg==0){
				System.err.println(readSales.get(i).getFileName()+"の商品コードが不正です。");
			}
		}
	}
}

class Branch extends Definition{
	private String branchName;
	public Branch(){}
	public Branch(String code, String defFileName) {
		super(code, defFileName);
	}
	public String getBranchName(){
		return branchName;
	}
	/**
	 * フォーマットチェック。
	 */
	@Override
	boolean checkFormat(String lineList, int i){
		if(i==0 && !(lineList.matches("\\d{3}"))){
			return false;
		}
		return true;
	}
//	@Override
//	void calculateSales(List<Definition> branch, List<Sales> readSales){
//		final long MAX_RANGE = 1_000_000_000L;
//		for(int i=0;i<readSales.size();i++){
//			int branchFlg = 0;
//			int branchSuffix = -1;
//			for(int j=0;j<branch.size();j++){
//				if(branch.get(j).getCode().equals(readSales.get(i).getBranchCode())){
//					branchFlg = 1;
//					branchSuffix = j;
//				}
//			}
//			if(branchFlg==1){
//				branch.get(branchSuffix).calculate(readSales.get(i).getSales());
//				if(MAX_RANGE<=branch.get(branchSuffix).getSales()){
//					System.err.println("合計金額が10桁を越えました。処理を終了します。");
//					System.exit(1);
//				}
//			}else{
//				System.err.println(readSales.get(i).getFileName()+"の支店コードが不正です。");
//			}
//		}
//	}
	static void writeOutputBranchFile(String path, List<? extends Definition> branch, String name) throws IOException{
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(path+"/"+name+".out"));
			for(int i=0;i<branch.size();i++){
				bw.write(branch.get(i).getCode()+","
						+branch.get(i).getBranchName()+","		// 欲しいListのジェネリクス型が親と子が違う
						+branch.get(i).getSales()+"\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			bw.close();
		}
	}
}

class Commodity extends Definition{
	private String commodityName;
	public Commodity(){}
	public Commodity(String code, String defFileName){
		super(code, defFileName);
	}
	public String getCommodityName(){
		return commodityName;
	}
	@Override
	public boolean checkFormat(String lineList, int i){
		if(i==0 && !(lineList.matches("[0-9a-zA-Z]{8}"))){
			return false;
		}
		return true;
	}
//	@Override
//	void calculateSales(List<Definition> commodity, List<Sales> readSales){
//		final long MAX_RANGE = 1_000_000_000L;
//		for(int i=0;i<readSales.size();i++){
//			int commodityFlg = 0;
//			int commoditySuffix = -1;
//			for(int j=0;j<commodity.size();j++){
//				if(commodity.get(j).getCode().equals(readSales.get(i).getBranchCode())){
//					commodityFlg = 1;
//					commoditySuffix = j;
//				}
//			}
//			if(commodityFlg==1){
//				commodity.get(commoditySuffix).calculate(readSales.get(i).getSales());
//				if(MAX_RANGE<=commodity.get(commoditySuffix).getSales()){
//					System.err.println("合計金額が10桁を越えました。処理を終了します。");
//					System.exit(1);
//				}
//			}else{
//				System.err.println(readSales.get(i).getFileName()+"の商品コードが不正です。");
//			}
//		}
//	}
	static void writeOutputCommodityFile(String path, List<Definition> commodity, String name) throws IOException{
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(path+"/"+name+".out"));
			for(int i=0;i<commodity.size();i++){
				bw.write(commodity.get(i).getCode()+","
						+commodity.get(i).getCommodityName()+","
						+commodity.get(i).getSales()+"\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			bw.close();
		}
	}
}