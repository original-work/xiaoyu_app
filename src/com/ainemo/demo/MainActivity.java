package com.ainemo.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ainemo.sdk.NemoSDK;
import com.ainemo.sdk.NemoSDKListener;
import com.ainemo.sdk.callback.CreateMeetingCallback;
import com.ainemo.sdk.callback.MakeCallH323Callback;
import com.ainemo.sdk.callback.MakeCallMeetingCallback;
import com.ainemo.sdk.callback.MakeCallNemoCallback;
import com.ainemo.sdk.model.CreateMeetingParam;
import com.ainemo.sdk.model.Meeting;
import com.ainemo.sdk.model.Nemo;
import com.ainemo.sdk.model.Result;
import com.ainemo.sdk.model.User;
import com.ainemo.shared.call.CallState;

public class MainActivity extends Activity {

	static final String T = "AndroidSDKSample";

	private NemoSDK nemoSDK; // 小鱼SDK

	private TextView logTextView;
	private EditText h323NumberEditText;

	private EditText number_edittextcallxiaoyu;
	private EditText number_edittextcallyunhuiyi1;
	private EditText number_edittextcallyunhuiyi2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {

		logTextView = (TextView) findViewById(R.id.log);
		h323NumberEditText = (EditText) findViewById(R.id.h323_number_edittext);
		number_edittextcallxiaoyu = (EditText) findViewById(R.id.number_edittextcallxiaoyu);
		number_edittextcallyunhuiyi1 = (EditText) findViewById(R.id.number_edittextcallyunhuiyi1);
		number_edittextcallyunhuiyi2 = (EditText) findViewById(R.id.number_edittextcallyunhuiyi2);

		nemoSDK = NemoSDK.getInstance();

		if (nemoSDK.getSettings() != null) {
			String e1 = nemoSDK.getSettings().isDebug() ? "开发环境 " : "生产环境 ";
			String e2 = nemoSDK.getSettings().isLoginMode() ? "登录模式 "
					: "非登录模式 ";
			String e3 = nemoSDK.getSettings().isPrivateCloudMode() ? "私有云 "
					: "";
			((TextView) findViewById(R.id.env)).setText("Settings:  " + e1 + e2
					+ e3);
		}

		// 会议事件通知
		nemoSDK.setListener(new NemoSDKListener() {
			@Override
			public void onParticipantChange(int participantsCount) { // 与会者人数变化事件
				Log.i(T, "onParticipantChange: current participantsCount:"
						+ participantsCount);
			}

			@Override
			public void onCallStateChange(CallState callState) { // 呼叫状态改变事件
				Log.i(T, "onCallStateChange: " + callState.name());
			}
		});

		// 创建云会议室
		Button createMeetingBtn = (Button) findViewById(R.id.create_conf);

		createMeetingBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logTextView.setText("");
				long start_time = System.currentTimeMillis();
				long duration = 3600 * 1000;
				String meetingName = "AndroidSDK_Meeting_Test";
				int maxParticipantCount = 25;
				boolean requirePassword = false;

				final CreateMeetingParam confParam = new CreateMeetingParam(
						start_time, duration, meetingName, maxParticipantCount,
						requirePassword);

				nemoSDK.createMeeting(confParam, new CreateMeetingCallback() {
					@Override
					public void onDone(Meeting meeting, Result result) {
						if (result.isSucceed()) {

							Log.i(T, "onCreateMeetingResult: " + meeting
									+ " , Result: " + result);

							String meetingPassword = "";
							if (meeting.getPassword() != null
									&& meeting.getPassword().trim().length() > 0) {
								meetingPassword = meeting.getPassword();
							} else {
								meetingPassword = "无";
							}

							logTextView.setText("创建会议成功, 会议号："
									+ meeting.getNumber() + ", 密码:"
									+ meetingPassword);
						} else {
							Log.i(T, "onCreateMeetingResult: null"
									+ " , Result: " + result);
							logTextView.setText("创建会议失败, 原因:" + result);
						}
					}
				});
			}
		});

		// ==============================================非登录模式=================================================<<<

		// 呼叫云会议室(非登录模式)
		Button makeCallConferenceAnonymouslyBtn = (Button) findViewById(R.id.make_call_anonymously);
		makeCallConferenceAnonymouslyBtn
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						User me = new User();
						me.setDisplayName("测试TestSDK960");
						me.setExternalUserId(""); // 第三方系统的UserId，保证唯一性*

						String number_yunhuiyi1 = number_edittextcallyunhuiyi1
								.getText().toString();
						String number_yunhuiyi2 = number_edittextcallyunhuiyi2
								.getText().toString();

						// 如果不需要回调，callback设置null即可
						nemoSDK.makeCallMeeting(new Meeting(number_yunhuiyi1,
								number_yunhuiyi2), me,
								new MakeCallMeetingCallback() {
									@Override
									public void onDone(Meeting meeting,
											Result result) {
										Log.i(T,
												"makeCallMeeting onDone, meeting: "
														+ meeting
														+ " , result: "
														+ result);
									}
								});
					}
				});

		// 呼叫小鱼号(非登录模式)
		Button makeCallNemoAnonymouslyBtn = (Button) findViewById(R.id.make_call_nemo_anonymously);
		makeCallNemoAnonymouslyBtn
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						User me = new User();
						me.setDisplayName("测试SDK—960");
						me.setExternalUserId(""); // 第三方系统的UserId，保证唯一性*

						String number_xiaoyu = number_edittextcallxiaoyu
								.getText().toString();

						// 如果不需要回调，callback设置null即可
						nemoSDK.makeCallNemo(new Nemo(number_xiaoyu), me,
								new MakeCallNemoCallback() {
									@Override
									public void onDone(Nemo nemo, Result result) {
										Log.i(T, "makeCallNemo onDone, nemo: "
												+ nemo + " , result: " + result);
									}
								});
					}
				});

		// 呼叫H323设备(非登录模式)
		findViewById(R.id.make_call_h323_anonymously).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						User me = new User();
						me.setDisplayName("测试SDK—160");
						me.setExternalUserId(""); // 第三方系统的UserId，保证唯一性*

						String h323Number = h323NumberEditText.getText()
								.toString();
						// 如果不需要回调，callback设置null即可
						nemoSDK.makeCallH323(h323Number, me,
								new MakeCallH323Callback() {
									@Override
									public void onDone(String number,
											Result result) {
										Log.i(T,
												"makeCallH323 onDone, h323Number: "
														+ number
														+ " , result: "
														+ result);
									}
								});
					}
				});

		// ================================================登录模式=================================================<<<

		// 呼叫云会议室(登录模式)
		Button makeCallMeetingBtn = (Button) findViewById(R.id.make_call_meeting);
		makeCallMeetingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				User me = new User();
				me.setSecurityKey("5df9d17699d30dd4a4498bfadfb23c0c1540331a838");
				me.setCellPhone("+86-960");
				me.setDisplayName("测试SDK—960");
				me.setId(2709);
				me.setProfilePicture("1395-a5d8917e-6765-437f-b689-250880016e4b-1442906111412");

				// 如果不需要回调，callback设置null即可
				nemoSDK.makeCallMeeting(new Meeting("918612438628", ""), me,
						new MakeCallMeetingCallback() {
							@Override
							public void onDone(Meeting meeting, Result result) {
								Log.i(T, "makeCallMeeting onDone, meeting: "
										+ meeting + " , result: " + result);
							}
						});
			}
		});

		// 呼叫小鱼号(登录模式)
		Button makeCallNemoBtn = (Button) findViewById(R.id.make_call_nemo);
		makeCallNemoBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				User me = new User();
				me.setSecurityKey("5df9d17699d30dd4a4498bfadfb23c0c1540331a838");
				me.setCellPhone("+86-960");
				me.setDisplayName("Test SDK—960");
				me.setId(2709);
				me.setProfilePicture("1395-a5d8917e-6765-437f-b689-250880016e4b-1442906111412");

				// 如果不需要回调，callback设置null即可
				nemoSDK.makeCallNemo(new Nemo("123456"), me,
						new MakeCallNemoCallback() {
							@Override
							public void onDone(Nemo nemo, Result result) {
								Log.i(T, "makeCallNemo onDone, nemo: " + nemo
										+ " , result: " + result);
							}
						});
			}
		});

	}

}
