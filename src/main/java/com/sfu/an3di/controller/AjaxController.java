package com.sfu.an3di.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfu.an3di.ApplicationConfig;
import com.sfu.an3di.utils.ApiInvoker;
import com.sfu.an3di.utils.CommonUtils;

@RestController
public class AjaxController {
	
	@Autowired
	private ApplicationConfig appConfig;
	
	@Autowired
	private ApiInvoker apiInvoker;
	
	private Logger logger = CommonUtils.getLogger();
	
	@RequestMapping("/predict")
    public Map<String, String> listInterview(Model m, 
    							@RequestParam(value = "stockId", defaultValue = "") String stockId,
                                @RequestParam(value = "predictDate",defaultValue = "") String predictDate) throws Exception {
		
        Map<String, String> retData = new HashMap<>();
        
        try {
    		String s = "";
    		
			//Process p = Runtime.getRuntime().exec("python3 test.py + ");
    		
			ProcessBuilder pb = new ProcessBuilder("python3", "test.py", stockId, predictDate);
			Process p = pb.start();
			
			BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            // read the output from the command
            logger.info("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
            	logger.info(s);
            }
            
            // read any errors from the attempted command
            logger.info("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
            	logger.info(s);
            }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        retData.put("low", "123.12");
        retData.put("high", "156.32");
        
        return retData;
    }
}
