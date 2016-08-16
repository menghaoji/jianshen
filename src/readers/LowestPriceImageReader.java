package readers;

import beans.SampleBinary;
import examples.PropertyReader;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LowestPriceImageReader extends BaseImageReader {
//	private static Logger logger = Logger
//			.getLogger(LowestPriceImageReader.class);
	//private Map<BufferedImage, String> trainMap = null;
	private int index = 0;
        //private int maxLength = 0;

	@Override
	public int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if ((color.getRed() + color.getGreen() + color.getBlue()) / 3 <= 200) {
			return 1;
		}
		return 0;
	}

	@Override
	public int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if ((color.getRed() + color.getGreen() + color.getBlue()) / 3 > 200) {
			return 1;
		}
		return 0;
	}

	@Override
	public String getAllOcr(BufferedImage img) throws Exception {
		return super.getAllOcr(img)+"00";
	}

	// public BufferedImage removeBackgroud(String picFile)
	// throws Exception {
	// BufferedImage img = ImageIO.read(new File(picFile));
	// return img;
	// }
//        public LowestPriceImageReader(){
//              maxLength = Integer.valueOf(PropertyReader.getProperty("img.maxLength"));
//        }
	

	public List<BufferedImage> splitImage(BufferedImage img) throws Exception {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
		int width = img.getWidth();
		int height = img.getHeight();
		List<Integer> weightlist = new ArrayList<Integer>();
		for (int x = 0; x < width; ++x) {
			int count = 0;
			for (int y = 0; y < height; ++y) {
				if (isBlack(img.getRGB(x, y)) == 1) {
					count++;
				}
			}
			weightlist.add(count);
		}
		for (int i = 0; i < weightlist.size();) {
			int length = 0;
			while (weightlist.get(i++) > 0) {
				length++;
			}
			if (length > 4) {
                                BufferedImage tmpImg = removeBlank(img.getSubimage(i - length - 1, 0,
						length, height));
                                //如果截取的图片的宽度过宽,说明没有两个或者多个数字没有完全分开,抛出异常,结束此次识别
//                                if(exceedWidth(tmpImg)){
//                                    throw new Exception("wrong split");
//                                }
				subImgs.add(tmpImg);
                                
				//subImgs.add(removeBlank(img.getSubimage(i - length / 2 - 1, 0,length / 2, height)));
				if(subImgs.size()==3){
                                        //截取到三个数字就结束,最后总是00不需要识别
					break;
				}
			}

//			else if (length > 3) {
//				subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0,
//						length, height)));
//			}
		}
//            if (subImgs.size() != 5) {
//                //如果截取的少于3, 则数字没有完全分割,抛出异常
//                throw new Exception("wrong split");
//            }
		//check
		//RegUtils.writeSample(subImgs);
		//return subImgs.subList(0, 3);
                
                return subImgs;
	}

	@Override
	public Map<SampleBinary, String> loadTrainData() throws Exception {
		if (RegConst.priceTrainMap == null) {
			Map<SampleBinary, String> map = new HashMap<SampleBinary, String>();
			File dir = new File("pricesample");
                    List<File> files = Arrays.asList(new File("pricesample").listFiles(new FileFilter(){
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith("png");
				}				
			}));
                    Collections.sort(files, new Comparator<File>() {
                        @Override
                        public int compare(File o1, File o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    for (File file : files) {
			map.put(binarize(ImageIO.read(file)), file.getName().charAt(0) + "");
		    }
	            RegConst.priceTrainMap = map;
		}
                
                return RegConst.priceTrainMap;
	}
	
	
	@Override
	public String getSingleCharOcr(int index,BufferedImage img,
			Map<SampleBinary, String> map) {
            //System.out.println("==================================start  "+index);
		String result = "";
		int width = img.getWidth();
		int height = img.getHeight();
		int min = width * height;
		for (SampleBinary bi : map.keySet()) {
			if(index == 0&&Integer.valueOf(map.get(bi))<8){
				continue;
			}
			int count = 0;
			int widthmin = width < bi.width ? width : bi.width;
			int heightmin = height < bi.height ? height : bi.height;
			Label1: for (int x = 0; x < widthmin; ++x) {
				for (int y = 0; y < heightmin; ++y) {
					if (isBlack(img.getRGB(x, y)) != bi.samples[x][y]) {
						count++;
						if (count >= min){
                                                        //System.out.println("index =    "+index+" sample = "+map.get(bi)+"count=  "+count+" min = "+min);
							break Label1;
                                                } 
					}
				}
			}
			if (count < min) {
                                //System.out.println("index =    "+index+" sample = "+map.get(bi)+"count!!!!!!!=  "+count+" min = "+min);
							
				//logger.debug(index+"result = "+map.get(bi)+"count="+count);
				min = count;
				result = map.get(bi);
			}
		}
                //System.out.println("==================================end  "+index+" result = "+result+" min = "+min);
		return result;
	}
        
        public static void main(String[] args){
            //test
            String num = "83000";
		BaseImageReader imageReader = new LowestPriceImageReader();
		String imgPath = "price/"+num+".png";  
		BufferedImage bufferImg;
		String result;
		try {
				bufferImg = ImageIO.read(new FileInputStream(imgPath));
				result = imageReader.getAllOcr(bufferImg);
								
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }

   
    
}
