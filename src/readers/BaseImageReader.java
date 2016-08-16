package readers;

import beans.SampleBinary;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class BaseImageReader {
        private static Logger logger = Logger
			.getLogger(BaseImageReader.class);
	public abstract int isBlack(int colorInt);

	public abstract int isWhite(int colorInt);

	public abstract Map<SampleBinary, String> loadTrainData() throws Exception;
	public BufferedImage removeBlank(BufferedImage img) throws Exception {
		int width = img.getWidth();
		int height = img.getHeight();
		int start = 0;
		int end = 0;
		Label1: for (int y = 0; y < height; ++y) {
			int count = 0;
			for (int x = 0; x < width; ++x) {
				if (isBlack(img.getRGB(x, y)) == 1) {
					count++;
				}
				if (count >= 1) {
					start = y;
					break Label1;
				}
			}
		}
		Label2: for (int y = height - 1; y >= 0; --y) {
			int count = 0;
			for (int x = 0; x < width; ++x) {
				if (isBlack(img.getRGB(x, y)) == 1) {
					count++;
				}
				if (count >= 1) {
					end = y;
					break Label2;
				}
			}
		}
		return img.getSubimage(0, start, width, end - start + 1);
	}
        //去色，返回 0,1 表示白黑
    public SampleBinary binarize(BufferedImage img) {
        SampleBinary binary = new SampleBinary();
        int width = img.getWidth();
        int height = img.getHeight();
        binary.width = width;
        binary.height = height;
        binary.samples = new int[width][height];
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                binary.samples[i][j] = isBlack(img.getRGB(i, j));
            }
        }
        return binary;
    }

	public String getAllOcr(BufferedImage img) throws Exception {
                String result = "";
                if(img == null){
                    return "";
                }
		//logger.info("start getAllOcr");
		List<BufferedImage> listImg = splitImage(img);
		Map<SampleBinary, String> map = loadTrainData();
		
			int i = 0;
			for (BufferedImage bi : listImg) {
				result += getSingleCharOcr(i++, bi, map);
			}

		// ImageIO.write(img, "JPG", new File("result2//" + result + ".jpg"));
		//System.out.println("result=" + result);
                //logger.info("end getAllOcr  "+result);
		return result;
	}

	public abstract String getSingleCharOcr(int index, BufferedImage bi, Map<SampleBinary, String> map);
	
	public abstract List<BufferedImage> splitImage(BufferedImage img) throws Exception;

}
