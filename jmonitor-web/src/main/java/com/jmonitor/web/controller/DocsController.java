package com.jmonitor.web.controller;

import com.jfinal.core.Controller;

/**
 * EventController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class DocsController extends Controller {
	public void index() {
		render("/docs.jsp");
	}
}


