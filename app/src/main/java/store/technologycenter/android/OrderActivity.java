package store.technologycenter.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import store.technologycenter.android.configurations.TechCenterConfiguration;
import store.technologycenter.android.http.Request;
import store.technologycenter.android.http.Response;
import store.technologycenter.android.models.Area;
import store.technologycenter.android.models.Coupon;
import store.technologycenter.android.models.Customer;
import store.technologycenter.android.models.PaymentMethod;
import store.technologycenter.android.services.CartService;
import store.technologycenter.android.services.CustomerService;
import store.technologycenter.android.services.ErrorService;
import store.technologycenter.android.services.TechCenterNetworkService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class OrderActivity extends AppCompatActivity {

    private Area mDeliveryArea = null;
    private PaymentMethod mPaymentMethod = null;
    private Coupon mCoupon = null;
    private boolean mIsDelivery = true;

    private TabLayout mTabLayoutOrderType;
    private LinearLayout mLinearLayoutDeliveryAddress;
    private TextView mTextWarning;

    private TextView mTextSubtotal;
    private TextView mTextDeliveryFee;
    private TextView mTextDeliveryTime;
    private TextView mTextDiscount;
    private TextView mTextCouponDescription;


    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPhone;
    private EditText mEditTextAddress;
    private EditText mEditTextComment;

    private Button mDeliverAreaButton;
    private Button mAddCouponButton;
    private Button mPaymentMethodButton;
    private Button mSubmitButton;
    private Realm mRealm;
    private Context mContext;
    private ExecutorService mExecutor;
    private Handler mHandler;
    private Customer mCustomer;
    private ActionBar mActionBar;
    private ProgressDialog mProgressDialog;
    private static final float ACTION_BAR_ELEVATION = 0;
    private static final String TAG = "ActivityOrderBinding";
    private static final int DELIVERY_TAB_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mRealm = Realm.getDefaultInstance();
        mContext = this;
        mCustomer = CustomerService.getCustomer(mRealm);
        mExecutor = Executors.newSingleThreadExecutor();
        mActionBar = getSupportActionBar();
        configureActionBar();
        setTitle(getString(R.string.submit_order));
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCancelable(false);

        mTabLayoutOrderType = findViewById(R.id.tab_layout_order_type);
        mLinearLayoutDeliveryAddress = findViewById(R.id.linear_layout_delivery_address);
        mSubmitButton = findViewById(R.id.btn_submit_order);
        mAddCouponButton = findViewById(R.id.btn_add_coupon);
        mDeliverAreaButton = findViewById(R.id.btn_area);
        mPaymentMethodButton = findViewById(R.id.btn_payment_method);
        mTextWarning = findViewById(R.id.text_warning);
        mTextSubtotal = findViewById(R.id.text_subtotal);
        mTextDeliveryFee = findViewById(R.id.text_delivery_fee);
        mTextDeliveryTime = findViewById(R.id.text_delivery_time);
        mTextDiscount = findViewById(R.id.text_discount);
        mTextCouponDescription = findViewById(R.id.text_coupon_description);
        mEditTextName = findViewById(R.id.et_name);
        mEditTextName = findViewById(R.id.et_name);
        mEditTextEmail = findViewById(R.id.et_email);
        mEditTextPhone = findViewById(R.id.et_phone);
        mEditTextAddress = findViewById(R.id.et_address);
        mEditTextComment = findViewById(R.id.et_comment);

        mAddCouponButton.setOnClickListener(v -> showCouponDialog());

        mEditTextName.setText(mCustomer.getName());
        mEditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRealm.executeTransaction(r -> mCustomer.setName(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditTextEmail.setText(mCustomer.getEmail());
        mEditTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRealm.executeTransaction(r -> mCustomer.setEmail(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditTextPhone.setText(mCustomer.getPhoneNumber());
        mEditTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRealm.executeTransaction(r -> mCustomer.setPhoneNumber(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mEditTextAddress.setText(mCustomer.getAddress());
        mEditTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRealm.executeTransaction(r -> mCustomer.setAddress(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mEditTextComment.setText(mCustomer.getComment());
        mEditTextComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRealm.executeTransaction(r -> mCustomer.setComment(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mTabLayoutOrderType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mIsDelivery = tab.getPosition() == DELIVERY_TAB_POSITION;
                configureDeliveryLayout();
                mPaymentMethod = null;
                mCoupon = null;
                mPaymentMethodButton.setText(getString(R.string.payment_method));
                mAddCouponButton.setText(getString(R.string.add_coupon));
                configureSubmitLayout();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mIsDelivery = mTabLayoutOrderType.getSelectedTabPosition() == DELIVERY_TAB_POSITION;
        configureDeliveryLayout();
        checkStoreStatus();
        configureSubmitLayout();
        mDeliverAreaButton.setOnClickListener(v -> showAreasDialog());
        mPaymentMethodButton.setOnClickListener(v -> showPaymentMethodsDialog());

        mSubmitButton.setOnClickListener(v -> submitOrder());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private double getSubtotal() {
        return CartService.getTotal(mRealm);
    }

    private double getTotal() {
        double discount = 0;
        if (mCoupon != null) {
            discount = (getSubtotal() * mCoupon.getPercentage()) / 100;
        }

        if (mIsDelivery) {
            return (mDeliveryArea != null ? getSubtotal() + mDeliveryArea.getFee() : getSubtotal()) - discount;
        }
        return getSubtotal() - discount;
    }

    private RealmResults<PaymentMethod> getPaymentMethods() {
        return mRealm.where(PaymentMethod.class).findAll();
    }

    private RealmResults<Area> getAreas() {
        return mRealm.where(Area.class).findAll();
    }

    private String[] getAreasArray() {
        RealmResults<Area> areas = getAreas();
        String[] areasArray = new String[areas.size()];
        for (int i = 0; i < areas.size(); i++) {
            Area _area = areas.get(i);
            if (_area != null) {
                areasArray[i] = _area.getName();
            }
        }
        return areasArray;
    }

    private String[] getPaymentMethodsArray() {
        RealmResults<PaymentMethod> methods = getPaymentMethods();
        String[] paymentMethodsArray = new String[methods.size()];
        for (int i = 0; i < methods.size(); i++) {
            PaymentMethod method = methods.get(i);
            if (method != null) {
                paymentMethodsArray[i] = method.getName();
            }
        }
        return paymentMethodsArray;
    }

    @SuppressLint("SetTextI18n")
    private void configureSubmitLayout() {

        String btnText = getString(R.string.submit_order) + " - ";
        double total = getTotal();

        double subtotal = getSubtotal();
        String subtotalString = getString(R.string.subtotal) + ": ";
        double discount = 0;
        if (mCoupon == null) {
            mTextDiscount.setVisibility(View.GONE);
            mTextCouponDescription.setVisibility(View.GONE);
        } else {
            discount = (subtotal * mCoupon.getPercentage()) / 100;
            mTextDiscount.setVisibility(View.VISIBLE);
            mTextCouponDescription.setVisibility(View.VISIBLE);
            mTextDiscount.setText(getString(R.string.discount) + ": -" + TechCenterConfiguration.BLRFormat(discount));
            mTextCouponDescription.setText(mCoupon.getDescription());
        }


        String totalString = TechCenterConfiguration.BLRFormat(total);
        subtotalString += TechCenterConfiguration.BLRFormat(subtotal);
        if (mIsDelivery) {
            mTextDeliveryFee.setVisibility(View.VISIBLE);
            mTextDeliveryTime.setVisibility(View.VISIBLE);
            mTextSubtotal.setVisibility(View.VISIBLE);
            if (mDeliveryArea != null) {
                btnText += (mDeliveryArea.getFee() > 0 ? totalString : getString(R.string.on_request));
//                subtotalString += mDeliveryArea.getFee() > 0 ? TechCenterConfiguration.BLRFormat(subtotal)
//                        : getString(R.string.on_request);
                mTextDeliveryTime.setText(getString(R.string.delivery_time) + ": " + mDeliveryArea.getViewTime());
                mTextDeliveryFee.setText(
                        mDeliveryArea.getFee() > 0 ? getString(R.string.delivery_fee) + ": " + mDeliveryArea.getViewFee()
                                : getString(R.string.delivery_fee) + ": " + getString(R.string.on_request)
                );
            } else {
                btnText += totalString;
                mTextDeliveryTime.setText(getString(R.string.delivery_time) + ": 0 min");
                mTextDeliveryFee.setText(
                        getString(R.string.delivery_fee) + ": " + TechCenterConfiguration.BLRFormat(0)
                );

            }
        } else {
            btnText += totalString;
            subtotalString += TechCenterConfiguration.BLRFormat(subtotal);
            mTextDeliveryFee.setVisibility(View.GONE);
            mTextDeliveryTime.setVisibility(View.GONE);
            mTextSubtotal.setVisibility(View.GONE);
        }

        mTextSubtotal.setText(subtotalString);
        mSubmitButton.setText(btnText);
    }

    private void configureDeliveryLayout() {
        mLinearLayoutDeliveryAddress.setVisibility(mIsDelivery ? View.VISIBLE : View.GONE);
    }


    private void showAreasDialog() {
        AlertDialog.Builder areaAlertDialogBuilder = new AlertDialog.Builder(mContext);

        areaAlertDialogBuilder.setTitle(getString(R.string.select_area));
        areaAlertDialogBuilder.setIcon(R.drawable.ic_location_outline);
        areaAlertDialogBuilder.setSingleChoiceItems(getAreasArray(), -1, (dialog, item) -> {
            mDeliveryArea = getAreas().get(item);
            if (mDeliveryArea != null) {
                mDeliverAreaButton.setText(mDeliveryArea.getName());
                configureSubmitLayout();
            }

            dialog.dismiss();
        });
        areaAlertDialogBuilder.setNegativeButton(getString(R.string.close), (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = areaAlertDialogBuilder.create();
        ListView listView = alertDialog.getListView();
        listView.setDivider(new ColorDrawable(Color.rgb(119, 119, 119)));
        listView.setDividerHeight(1);
        alertDialog.show();
        Button negativeBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeBtn.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        negativeBtn.setAllCaps(false);
    }

    private void showPaymentMethodsDialog() {
        AlertDialog.Builder paymentMethodAlertDialogBuilder = new AlertDialog.Builder(mContext);

        paymentMethodAlertDialogBuilder.setTitle(getString(R.string.payment_method));
        paymentMethodAlertDialogBuilder.setIcon(R.drawable.ic_credit_card);

        paymentMethodAlertDialogBuilder.setSingleChoiceItems(getPaymentMethodsArray(), -1, (dialog, item) -> {
            mPaymentMethod = getPaymentMethods().get(item);
            if (mPaymentMethod != null) {
                mPaymentMethodButton.setText(mPaymentMethod.getName());
            }
            dialog.dismiss();
        });
        paymentMethodAlertDialogBuilder.setNegativeButton(getString(R.string.close), (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = paymentMethodAlertDialogBuilder.create();
        ListView listView = alertDialog.getListView();
        listView.setDivider(new ColorDrawable(Color.rgb(119, 119, 119)));
        listView.setDividerHeight(1);
        alertDialog.show();
        Button negativeBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeBtn.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        negativeBtn.setAllCaps(false);
    }

    private void checkStoreStatus() {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            String response = TechCenterNetworkService.getStoreStatus();

            mHandler.post(() -> {
                if (Response.isNull(response)) {
                    mTextWarning.setVisibility(View.GONE);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean isOpen = jsonObject.getBoolean("is_open");
                        mTextWarning.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                        mTextWarning.setText(jsonObject.getString("text"));
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        });
    }


    private void getCoupon(String code) {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            String response = TechCenterNetworkService.getCoupon(code, mIsDelivery);
            mHandler.post(() -> {
                mProgressDialog.dismiss();
                if (Response.isNull(response)) {
                    ErrorService.showErrorDialog(mContext, getString(R.string.invalid_coupon));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject couponObject = jsonObject.getJSONObject(Response.DATA);
                        mCoupon = new Coupon(
                                couponObject.getString("code"),
                                couponObject.getDouble("percentage"),
                                couponObject.getString("des")
                        );
                        configureSubmitLayout();
                        String addCouponButtonText = getString(R.string.coupon_code) + " " + mCoupon.getCode();
                        mAddCouponButton.setText(addCouponButtonText);
                        mAddCouponButton.setEnabled(false);

                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        });
    }


    private void showCouponDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.add_coupon));
        builder.setIcon(R.drawable.ic_coupon);
        View editTextView = getLayoutInflater().inflate(R.layout.dialog_coupon, null);
        builder.setView(editTextView);
        EditText editText = editTextView.findViewById(R.id.et_coupon_code);
        builder.setPositiveButton(getString(R.string.add), (dialog, id) -> {
            getCoupon(editText.getText().toString());
            dialog.dismiss();
            mProgressDialog.dismiss();
            mProgressDialog.show();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveBtn.setTextColor(ContextCompat.getColor(mContext, R.color.dark));
        positiveBtn.setAllCaps(false);
        Button negativeBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeBtn.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        negativeBtn.setAllCaps(false);

    }


    private void submitOrder() {
        if (!TechCenterNetworkService.isConnected(mContext)) {
            ErrorService.showErrorDialog(mContext, getString(R.string.no_internet_connection_error));
            return;
        }
        if (CartService.getTotalItems(mRealm) == 0) {
            ErrorService.showErrorDialog(mContext, getString(R.string.cart_empty));
            return;
        }
        if (mEditTextName.getText().toString().isEmpty()) {
            ErrorService.showErrorDialog(mContext, getString(R.string.error_name_required));
            return;
        }
        if (mEditTextPhone.getText().toString().isEmpty()) {
            ErrorService.showErrorDialog(mContext, getString(R.string.error_phone_required));
            return;
        }

        if (!mEditTextEmail.getText().toString().isEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(mEditTextEmail.getText().toString()).matches()) {
                ErrorService.showErrorDialog(mContext, getString(R.string.invalid_email));
                return;
            }
        }

        if (mIsDelivery) {
            if (mDeliveryArea == null) {
                ErrorService.showErrorDialog(mContext, getString(R.string.error_area));
                return;
            }
            if (mEditTextAddress.getText().toString().isEmpty()) {
                ErrorService.showErrorDialog(mContext, getString(R.string.error_address_required));
                return;
            }
        }
        if (mPaymentMethod == null) {
            ErrorService.showErrorDialog(mContext, getString(R.string.error_payment_method));
            return;
        }
        if (mEditTextComment.getText().toString().length() > 155) {
            ErrorService.showErrorDialog(mContext, getString(R.string.error_comment));
            return;
        }
        mProgressDialog.dismiss();
        mProgressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = orderJsonObject();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        mHandler = new Handler(Looper.getMainLooper());
        JSONObject finalJsonObject = jsonObject;
        mExecutor.execute(() -> {
            boolean isSuccess = TechCenterNetworkService.orderSubmit(finalJsonObject);
            mHandler.post(() -> {
                mProgressDialog.dismiss();
                if(isSuccess){
                    CartService.reset(mRealm);
                    showSuccessDialog();
                }else{
                    ErrorService.showErrorDialog(mContext, getString(R.string.error_order));
                }

            });
        });
    }


    private void showSuccessDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.success_order_title));
        builder.setMessage(getString(R.string.success_order_message));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.close), (dialog, id) -> {
            dialog.dismiss();
            onBackPressed();
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setOnCancelListener(dialog -> onBackPressed());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(mContext, R.color.dark));
    }


    private JSONObject orderJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", mEditTextName.getText().toString());
        jsonObject.put("email", mEditTextEmail.getText().toString());
        jsonObject.put("phone", mEditTextPhone.getText().toString());
        jsonObject.put("delivery", mIsDelivery);
        jsonObject.put("area", mDeliveryArea != null ? mDeliveryArea.getId() : null);
        jsonObject.put("address", mEditTextAddress.getText().toString());
        jsonObject.put("payment_method", mPaymentMethod != null ? mPaymentMethod.getId() : null);
        if (mCoupon != null) {
            jsonObject.put("coupon_code", mCoupon.getCode());
        }
        jsonObject.put("comment", mEditTextComment.getText().toString());
        jsonObject.put("source", TechCenterConfiguration.ANDROID_APP_SOURCE_NUMBER);

        JSONObject cartObject = new JSONObject();
        cartObject.put("items", CartService.getJsonArray(mRealm));
        cartObject.put("total", getSubtotal());
        jsonObject.put("cart", cartObject);

        return jsonObject;
    }


    private void configureActionBar() {
        if (mActionBar == null) return;
        mActionBar.setElevation(ACTION_BAR_ELEVATION);
    }
}