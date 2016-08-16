/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readers;

import beans.SampleBinary;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author menghaoji
 */
public class MyBidImageReader extends BaseImageReader{

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<SampleBinary, String> loadTrainData() throws Exception {
        if (RegConst.bidTrainMap == null) {
			Map<SampleBinary, String> map = new HashMap<SampleBinary, String>();
			File dir = new File("bidsamples");
                    List<File> files = Arrays.asList(new File("bidsamples").listFiles(new FileFilter(){
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
	            RegConst.bidTrainMap = map;
		}
                return RegConst.bidTrainMap;
    }

    @Override
    public String getSingleCharOcr(int index, BufferedImage img, Map<SampleBinary, String> map) {
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

    @Override
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
				subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0,
						length, height)));
				//subImgs.add(removeBlank(img.getSubimage(i - length / 2 - 1, 0,length / 2, height)));
				if(subImgs.size()==10){
					break;
				}
			} 
//			else if (length > 3) {
//				subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0,
//						length, height)));
//			}
		}
		//check
		//RegUtils.writeSample(subImgs);
		return subImgs;
    }
    
//    @Override
//	public String getAllOcr(BufferedImage img) throws Exception {
//		return super.getAllOcr(img)+"00";
//	}
        
//        public static void main(String[] args){
//            //test
//                String num = "84200";
//		BaseImageReader imageReader = new MyBidImageReader();
//		String imgPath = "bid/"+num+".png";  
//		BufferedImage bufferImg;
//		String result;
//		try {
//				bufferImg = ImageIO.read(new FileInputStream(imgPath));
//				result = imageReader.getAllOcr(bufferImg);
//				System.out.println("bid result = "+result);				
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        }
    
    @Override
	public String getAllOcr(BufferedImage img) throws Exception {
                if(img == null){
                    return "";
                }
		//logger.info("start getAllOcr");
		List<BufferedImage> listImg = splitImage(img);
                int i=0;
                for(BufferedImage im:listImg){
                    ImageIO.write(im, "png", new File("bidsamples/"+i+".png")); 
                    i++;
                }
		return "";
	}
        
        public static void main(String[] args){
            //test
            String num = "0123456789";
		MyBidImageReader imageReader = new MyBidImageReader();
		String imgPath = "bidsamples/"+num+".png";  
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
