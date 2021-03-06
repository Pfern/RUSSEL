/*
Copyright (c) 2012 Eduworks Corporation
All rights reserved.
 
This Software (including source code, binary code and documentation) is provided by Eduworks Corporation to
the Government pursuant to contract number W31P4Q-12 -C- 0119 dated 21 March, 2012 issued by the U.S. Army 
Contracting Command Redstone. This Software is a preliminary version in development. It does not fully operate
as intended and has not been fully tested. This Software is provided to the U.S. Government for testing and
evaluation under the following terms and conditions:

	--Any redistribution of source code, binary code, or documentation must include this notice in its entirety, 
	 starting with the above copyright notice and ending with the disclaimer below.
	 
	--Eduworks Corporation grants the U.S. Government the right to use, modify, reproduce, release, perform,
	 display, and disclose the source code, binary code, and documentation within the Government for the purpose
	 of evaluating and testing this Software.
	 
	--No other rights are granted and no other distribution or use is permitted, including without limitation 
	 any use undertaken for profit, without the express written permission of Eduworks Corporation.
	 
	--All modifications to source code must be reported to Eduworks Corporation. Evaluators and testers shall
	 additionally make best efforts to report test results, evaluation results and bugs to Eduworks Corporation
	 using in-system feedback mechanism or email to russel@eduworks.com.
	 
THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
THE COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN 
IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
*/

package com.eduworks.russel.ui.client;

import com.eduworks.gwt.russel.ui.client.net.AlfrescoCallback;
import com.eduworks.gwt.russel.ui.client.net.AlfrescoNullCallback;
import com.eduworks.gwt.russel.ui.client.net.AlfrescoPacket;
import com.eduworks.gwt.russel.ui.client.net.CommunicationHub;
import com.eduworks.russel.ui.client.pagebuilder.ScreenDispatch;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;

public class Russel extends Constants implements EntryPoint, ValueChangeHandler<String>
{
	public static String buildNumber;
	public static ScreenDispatch view = new ScreenDispatch();
	
	public static AlfrescoNullCallback<AlfrescoPacket> nonFunctional = new AlfrescoNullCallback<AlfrescoPacket>() {
																			@Override
																			public void onEvent(Event event) {
																				Window.alert(Constants.INCOMPLETE_FEATURE_MESSAGE);
																			}
																		};
	
	
	@Override
	public void onModuleLoad()
	{
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				Window.alert("onUncaughtException errors");
				Window.alert(e.toString());
				Window.alert(e.getCause().getMessage());
				e.printStackTrace();
			}
		});
		
		view.clearHistory();
		
		History.addValueChangeHandler(this);
				
		Window.addWindowClosingHandler(new ClosingHandler() {
			
			@Override
			public void onWindowClosing(ClosingEvent event) {
				// TODO Auto-generated method stub
				Window.alert("onWindowClosingMessage -- will address in polish sprint");
			}
		});
		CommunicationHub.sendHTTP(CommunicationHub.GET, 
								  "../js/module.properties", 
								  null, 
								  false,
								  new AlfrescoCallback<AlfrescoPacket>() {
									@Override
									public void onSuccess(AlfrescoPacket alfrescoPacket) {
										buildNumber = alfrescoPacket.getRawString().substring(alfrescoPacket.getRawString().lastIndexOf("=")+1);
										History.fireCurrentHistoryState();
									}
									
									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Couldn't find build number");
									}
								});
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		view.loadHistoryScreen(event.getValue());
	}
}