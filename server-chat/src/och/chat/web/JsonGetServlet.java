/*
 * Copyright 2015 Evgeny Dolganov (evgenij.dolganov@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package och.chat.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import och.chat.service.ChatsApp;
import och.chat.service.ChatsService;
import och.comp.web.BaseJsonGetServlet;

@SuppressWarnings("serial")
public abstract class JsonGetServlet<O> extends BaseJsonGetServlet<O>{
	
	protected ChatsApp app;
	protected ChatsService chats;
	
	
	@Override
	protected void baseInit(ServletConfig config) throws ServletException {
		app = ChatsAppProvider.get(getServletContext());
		chats = app.chats;
	}
	
	@Override
	protected WebSecurityProvider getSecurityProvider() {
		return app.security;
	}
	
}
