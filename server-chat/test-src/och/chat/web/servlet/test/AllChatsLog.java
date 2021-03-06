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
package och.chat.web.servlet.test;

import static och.api.model.user.SecurityContext.*;
import static och.util.Util.*;

import java.util.Collection;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import och.api.model.chat.ChatLog;
import och.chat.web.JsonGetServlet;

@SuppressWarnings("serial")
@WebServlet("/test/allChatsLog")
public class AllChatsLog extends JsonGetServlet<String> {
	
	@Override
	protected String doJsonGet(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		
		pushToSecurityContext_SYSTEM_USER();
		try {
			String accountId = req.getParameter("accountId");
			if( ! hasText(accountId)) {
				return getForAllAccs();
			} else {
				return getForAcc(accountId);
			}
		}finally {
			popUserFromSecurityContext();
		}

	}
	
	private String getForAcc(String accountId) {
		
		Info info = getInfoForAcc(accountId);
		if(info == null) return null;
		
		return "Acc: "+accountId+" Chats: "+info.chatsCount+" Msgs: "+info.msgCount;
	}
	
	private String getForAllAccs() {

		int totalChatsCount = 0;
		int totalMsgCount = 0;
		
		Collection<String> accIds = chats.getAccIds();
		for (String accId : accIds) {
			Info info = getInfoForAcc(accId);
			if(info != null){
				totalChatsCount += info.chatsCount;
				totalMsgCount += info.msgCount;
			}
		}
		
		return "Total chats: "+totalChatsCount+" Total msgs: "+totalMsgCount;
	}
	
	
	
	
	private static class Info {
		public final int chatsCount;
		public final int msgCount;
		public Info(int chatsCount, int msgCount) {
			this.chatsCount = chatsCount;
			this.msgCount = msgCount;
		}
	}
	
	private Info getInfoForAcc(String accountId){
		Collection<ChatLog> logs = chats.getAllActiveChatLogs(accountId);
		if(logs == null) return null;
		
		int msgCount = 0;
		for (ChatLog chatLog : logs) {
			msgCount += chatLog.messages == null? 0 : chatLog.messages.size();
		}
		
		return new Info(logs.size(), msgCount);
	}


}
