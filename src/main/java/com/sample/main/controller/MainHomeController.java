package com.sample.main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecbank.common.mvc.web.BaseController;

@Controller
public class MainHomeController extends BaseController {

	final static Logger logger = LoggerFactory.getLogger(MainHomeController.class);

	@RequestMapping(value="/main.do")
	public String main() {

	    System.out.println("aaaaaaaaaa Go : Main");

		return "main";
	}


}