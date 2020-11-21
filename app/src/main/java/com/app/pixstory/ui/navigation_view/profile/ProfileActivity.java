package com.app.pixstory.ui.navigation_view.profile;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.base.flex.FlexboxAdapter;
import com.app.pixstory.data.model.api.BioResponse;
import com.app.pixstory.data.model.api.Citation;
import com.app.pixstory.data.model.api.Education;
import com.app.pixstory.data.model.api.Integrity;
import com.app.pixstory.data.model.api.UserProfileResponse;
import com.app.pixstory.data.model.api.Work;
import com.app.pixstory.databinding.ActivityProfileBinding;
import com.app.pixstory.databinding.CameraGalleryDialogLayoutBinding;
import com.app.pixstory.ui.interests.Interests;
import com.app.pixstory.ui.navigation_view.faqs.FAQActivity;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.FileCompressor;
import com.app.pixstory.utils.General;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.Utils;
import com.app.pixstory.utils.web_view.WebViewActivity;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

public class ProfileActivity extends BaseActivity<ActivityProfileBinding, ProfileViewModel> implements ProfileNavigator, UpdatedWorkAdapter.WorkAction, UpdatedEducationAdapter.EducationAction, UpdatedCitationAdapter.CitationAction, CitationAdapter.CitationAction {


    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 200;
    private ActivityProfileBinding binding;
    private ProfileViewModel viewModel;
    private BottomSheetBehavior badgeSheetBehaviour, educationSheetBehaviour, integritySheetBehaviour, citationSheetBehaviour, workSheetBehaviour, bioSheetBehaviour, interestSheetBehaviour;
    private String isCurrent;
    private Dialog pickImageDialog;
    private UserProfileResponse.Data data;
    private FileCompressor mCompressor;
    private String fileName;
    private int pos;
    UpdatedWorkAdapter updatedWorkAdapter;
    UpdatedCitationAdapter updatedCitationAdapter;
    UpdatedEducationAdapter updatedEducationAdapter;
    boolean updateWork, updateCitation, updateEducation;
    private View seeMoreWork, seeMoreEducation, seeMoreCitation;

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void getBinding(ActivityProfileBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(ProfileViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<ProfileViewModel> setViewModel() {
        return ProfileViewModel.class;
    }

    @Override
    protected void init() {
        mCompressor = new FileCompressor(this);
        viewModel.setNavigator(this);
        setBadgeSheet();
        setEducationSheet();
        setIntegritySheet();
        setCitationSheet();
        setWorkSheet();
        setBioSheet();
        setInterestSheet();
        setOnclick();
        toolbar(binding.toolbar.toolbar, binding.toolbar.text, R.string.profile);

        viewModel.getUserProfileData();

        viewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

        binding.bottomSheetSetWork.type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.current:
                        binding.bottomSheetSetWork.current.setChecked(true);
                        isCurrent = "1";
                        break;
                    case R.id.previous:
                        binding.bottomSheetSetWork.previous.setChecked(true);
                        isCurrent = "0";
                        break;
                }
            }
        });
    }

    private void setOnclick() {
        binding.badgesDetail.setOnClickListener(this::OnClick);
        binding.integrityDetail.setOnClickListener(this::OnClick);
        binding.bottomSheetSetBadge.closeBadge.setOnClickListener(this::OnClick);
        binding.bottomSheetSetEducation.closeEducation.setOnClickListener(this::OnClick);
        binding.bottomSheetSetIntegrity.closeIntegrity.setOnClickListener(this::OnClick);
        binding.bottomSheetSetIntegrity.faq.setOnClickListener(this::OnClick);
        binding.educationEdit.setOnClickListener(this::OnClick);
        binding.citationEdit.setOnClickListener(this::OnClick);
        binding.bioEdit.setOnClickListener(this::OnClick);
        binding.bottomSheetSetCitation.closeCitation.setOnClickListener(this::OnClick);
        binding.workEdit.setOnClickListener(this::OnClick);
        binding.bottomSheetSetWork.closeWork.setOnClickListener(this::OnClick);
        binding.interestEdit.setOnClickListener(this::OnClick);
        binding.bottomSheetSetBio.closeBio.setOnClickListener(this::OnClick);

        binding.bottomSheetSetWork.sendWork.setOnClickListener(this::OnClick);
        binding.bottomSheetSetEducation.sendEducation.setOnClickListener(this::OnClick);
        binding.bottomSheetSetCitation.sendCitation.setOnClickListener(this::OnClick);
        binding.bottomSheetSetBio.sendBio.setOnClickListener(this::OnClick);
        binding.imageView.setOnClickListener(this::OnClick);

        binding.bottomSheetShowInterest.closeInterest.setOnClickListener(this::OnClick);
        binding.seeMoreLayout.setOnClickListener(this::OnClick);

    }

    private void setBioSheet() {
        bioSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetSetBio.bio);
        bioSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bioSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    private void setInterestSheet() {
        interestSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetShowInterest.interest);
        interestSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    interestSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    private void setBadgeSheet() {
        badgeSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetSetBadge.badge);
        badgeSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    badgeSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    private void setEducationSheet() {
        educationSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetSetEducation.education);
        educationSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    educationSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    private void setIntegritySheet() {
        integritySheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetSetIntegrity.integrity);
        integritySheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    integritySheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }


    private void setCitationSheet() {
        citationSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetSetCitation.citation);
        citationSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    citationSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    private void setWorkSheet() {
        workSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetSetWork.work);
        workSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    workSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    @SuppressLint("InlinedApi")
    private void OnClick(View view) {

        switch (view.getId()) {
            case 100:
                WorkAdapter workAdapter = new WorkAdapter(data.getWork(), data.getWork().size());
                binding.workList.setAdapter(workAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.workList);
                binding.workList.removeFooterView(seeMoreWork);
                break;
            case 200:
                EducationAdapter educationAdapter = new EducationAdapter(data.getEducation(), data.getEducation().size());
                binding.educationList.setAdapter(educationAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.educationList);
                binding.educationList.removeFooterView(seeMoreEducation);
                break;
            case 300:
                CitationAdapter citationAdapter = new CitationAdapter(this, data.getCitation(), data.getCitation().size());
                binding.citationList.setAdapter(citationAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.citationList);
                binding.citationList.removeFooterView(seeMoreCitation);
                break;
            case R.id.badgesDetail:
                badgeSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.integrityDetail:
                integritySheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.citationEdit:
                citationSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                cleanCitationSheet();
                break;
            case R.id.educationEdit:
                educationSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                cleanEducationSheet();
                break;
            case R.id.workEdit:
                workSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                cleanWorkSheet();
                break;
            case R.id.close_badge:
                badgeSheetBehaviour.setHideable(true);
                badgeSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.bioEdit:
                if (data.getBioText() != null && !data.getBioText().isEmpty()) {
                    binding.bottomSheetSetBio.bioText.setText(data.getBioText());
                    Utils.setFocus(binding.bottomSheetSetBio.bioText);
                }
                bioSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.close_bio:
                bioSheetBehaviour.setHideable(true);
                bioSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.close_education:
                educationSheetBehaviour.setHideable(true);
                educationSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.close_integrity:
                integritySheetBehaviour.setHideable(true);
                integritySheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.close_citation:
                citationSheetBehaviour.setHideable(true);
                citationSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.close_work:
                workSheetBehaviour.setHideable(true);
                workSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.interestEdit:
                startActivity(Interests.class);
                break;
            case R.id.faq:
                integritySheetBehaviour.setHideable(true);
                integritySheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                GlobalMethods.intentMethod(this, FAQActivity.class);
                break;

            case R.id.send_work:

                if (binding.bottomSheetSetWork.jobTitle.getText().toString().isEmpty()) {
                    showToast("Enter job title");
                    return;
                }

                if (binding.bottomSheetSetWork.organisation.getText().toString().isEmpty()) {
                    showToast("Enter organisation");
                    return;
                }
                if (isCurrent != null && isCurrent.isEmpty()) {
                    showToast("Select current or previous");
                    return;
                }

                if (updateWork) {
                    viewModel.updateDeleteWork(data.getWork().get(pos).getId(), Constants.EDIT, binding.bottomSheetSetWork.jobTitle.getText().toString().trim(), binding.bottomSheetSetWork.organisation.getText().toString().trim(), isCurrent);
                    updateWork = false;
                } else {
                    viewModel.addWorkExperience(binding.bottomSheetSetWork.jobTitle.getText().toString().trim(), binding.bottomSheetSetWork.organisation.getText().toString().trim(), isCurrent);
                }
                break;

            case R.id.send_education:

                if (binding.bottomSheetSetEducation.degree.getText().toString().isEmpty()) {
                    showToast("Enter degree");
                    return;
                }

                if (binding.bottomSheetSetEducation.university.getText().toString().isEmpty()) {
                    showToast("Enter university");
                    return;
                }

                if (binding.bottomSheetSetEducation.institute.getText().toString().isEmpty()) {
                    showToast("Enter institute");
                    return;
                }

                if (updateEducation) {
                    viewModel.updateDeleteEducation(data.getEducation().get(pos).getId(), Constants.EDIT, binding.bottomSheetSetEducation.degree.getText().toString().trim(), binding.bottomSheetSetEducation.university.getText().toString().trim(), binding.bottomSheetSetEducation.institute.getText().toString().trim());
                    updateEducation = false;
                } else {
                    viewModel.addEducation(binding.bottomSheetSetEducation.degree.getText().toString().trim(), binding.bottomSheetSetEducation.university.getText().toString().trim(), binding.bottomSheetSetEducation.institute.getText().toString().trim());
                }

                break;

            case R.id.send_citation:

                if (binding.bottomSheetSetCitation.description.getText().toString().isEmpty()) {
                    showToast("Enter description");
                    return;
                }

                if (binding.bottomSheetSetCitation.link.getText().toString().isEmpty()) {
                    showToast("Enter link");
                    return;
                }

                if (binding.bottomSheetSetCitation.description.getText().toString().length() > 125) {
                    showToast("Entered description should be less than 125 characters including spaces");
                    return;
                }

                if (!URLUtil.isValidUrl(binding.bottomSheetSetCitation.link.getText().toString())) {
                    showToast("Entered url is not a valid link");
                    return;
                }

                if (updateCitation) {
                    viewModel.updateDeleteCitation(data.getCitation().get(pos).getId(), Constants.EDIT, binding.bottomSheetSetCitation.description.getText().toString().trim(), binding.bottomSheetSetCitation.link.getText().toString().trim());
                    updateCitation = false;
                } else {
                    viewModel.addCitation(binding.bottomSheetSetCitation.description.getText().toString().trim(), binding.bottomSheetSetCitation.link.getText().toString().trim());
                }

                break;

            case R.id.send_bio:

                if (binding.bottomSheetSetBio.bioText.getText().toString().isEmpty()) {
                    showToast("Enter bio");
                    return;
                }

                if (binding.bottomSheetSetBio.bioText.getText().toString().length() > 100) {
                    showToast("Entered bio should be less than 100 characters including spaces");
                    return;
                }

                viewModel.addBio(binding.bottomSheetSetBio.bioText.getText().toString().trim());

                break;

            case R.id.imageView:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                } else {
                    pickImageDialog();
                }
                break;

            case R.id.camera:

                fileName = System.currentTimeMillis() + ".jpg";
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                }

                pickImageDialog.dismiss();
                break;

            case R.id.gallery:

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_GALLERY);

                pickImageDialog.dismiss();
                break;

            case R.id.cancel:
                pickImageDialog.dismiss();
                break;

            case R.id.see_more_layout:
                addInterestSheet(data.getInterest());
                interestSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.close_interest:
                interestSheetBehaviour.setHideable(true);
                interestSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

        }
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists())
            path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(ProfileActivity.this, getPackageName() + ".provider", image);
    }

    public void pickImageDialog() {
        pickImageDialog = new Dialog(this);
        pickImageDialog.setCancelable(false);
        CameraGalleryDialogLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.camera_gallery_dialog_layout, null, false);
        pickImageDialog.setContentView(binding.getRoot());
        pickImageDialog.show();
        binding.camera.setOnClickListener(this::OnClick);
        binding.gallery.setOnClickListener(this::OnClick);
        binding.cancel.setOnClickListener(this::OnClick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    CropImage.activity(uri).start(this);
                }
                break;

            case REQUEST_CAMERA:
                Uri uri = getCacheImagePath(fileName);
                CropImage.activity(uri).start(this);
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    uploadImage(resultUri);
                }
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                showToast("Crop error !!!");
                break;

        }

    }

    private void uploadImage(Uri selectedImage) {

        try {
            File mPhotoFile = mCompressor.compressToFile(new File(Objects.requireNonNull(Utils.getPath(this, selectedImage))));
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", mPhotoFile.getName(), requestFile);
            viewModel.uploadImage(body);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void workUpdated(List<Work> workList) {

        data.setWork(workList);

        binding.workEdit.setText("Edit");

        setWorkSeeMore(workList);

        updatedWorkAdapter = new UpdatedWorkAdapter(this, workList);
        binding.bottomSheetSetWork.workList.setAdapter(updatedWorkAdapter);
        Utils.setListViewHeightBasedOnChildren(binding.workList);

        cleanWorkSheet();

    }

    @SuppressLint("ResourceType")
    private void setWorkSeeMore(List<Work> workList) {
        WorkAdapter workAdapter = null;
        binding.workList.removeFooterView(seeMoreWork);

        if (workList.size() > 2) {
            workAdapter = new WorkAdapter(workList, 2);
            seeMoreWork = LayoutInflater.from(this).inflate(R.layout.see_more_text_layout, null);
            seeMoreWork.setId(100);
            seeMoreWork.setOnClickListener(this::OnClick);
            binding.workList.addFooterView(seeMoreWork);
            binding.workList.setAdapter(workAdapter);
            Utils.setListViewHeightBasedOnChildren(binding.workList);
        } else {
            workAdapter = new WorkAdapter(workList, workList.size());
            binding.workList.removeFooterView(seeMoreWork);
            binding.workList.setAdapter(workAdapter);
            Utils.setListViewHeightBasedOnChildren(binding.workList);
        }
    }

    @Override
    public void educationUpdated(List<Education> educationList) {

        data.setEducation(educationList);

        binding.educationEdit.setText("Edit");

        setEducationSeeMore(educationList);

        UpdatedEducationAdapter updatedEducationAdapter = new UpdatedEducationAdapter(this, educationList);
        binding.bottomSheetSetEducation.educationList.setAdapter(updatedEducationAdapter);
        Utils.setListViewHeightBasedOnChildren(binding.educationList);

        cleanEducationSheet();

    }

    @SuppressLint("ResourceType")
    private void setEducationSeeMore(List<Education> educationList) {
        EducationAdapter educationAdapter = null;
        binding.educationList.removeFooterView(seeMoreEducation);

        if (educationList.size() > 2) {
            educationAdapter = new EducationAdapter(educationList, 2);

            seeMoreEducation = LayoutInflater.from(this).inflate(R.layout.see_more_text_layout, null);
            seeMoreEducation.setId(200);
            seeMoreEducation.setOnClickListener(this::OnClick);
            binding.educationList.addFooterView(seeMoreEducation);
            binding.educationList.setAdapter(educationAdapter);
            Utils.setListViewHeightBasedOnChildren(binding.educationList);

        } else {

            educationAdapter = new EducationAdapter(educationList, educationList.size());
            binding.educationList.removeFooterView(seeMoreEducation);
            binding.educationList.setAdapter(educationAdapter);
            Utils.setListViewHeightBasedOnChildren(binding.educationList);

        }
    }


    @Override
    public void citationUpdated(List<Citation> citationList) {

        data.setCitation(citationList);

        binding.citationEdit.setText("Edit");

        setCitationSeeMore(citationList);

        UpdatedCitationAdapter updatedCitationAdapter = new UpdatedCitationAdapter(this, citationList);
        binding.bottomSheetSetCitation.citationList.setAdapter(updatedCitationAdapter);
        Utils.setListViewHeightBasedOnChildren(binding.citationList);

        cleanCitationSheet();
    }

    @SuppressLint("ResourceType")
    private void setCitationSeeMore(List<Citation> citationList) {
        CitationAdapter citationAdapter = null;
        binding.citationList.removeFooterView(seeMoreCitation);

        if (citationList.size() > 2) {
            citationAdapter = new CitationAdapter(this, citationList, 2);

            seeMoreCitation = LayoutInflater.from(this).inflate(R.layout.see_more_text_layout, null);
            seeMoreCitation.setId(300);
            seeMoreCitation.setOnClickListener(this::OnClick);
            binding.citationList.addFooterView(seeMoreCitation);
            binding.citationList.setAdapter(citationAdapter);
            Utils.setListViewHeightBasedOnChildren(binding.citationList);

        } else {

            citationAdapter = new CitationAdapter(this, citationList, citationList.size());
            binding.citationList.setAdapter(citationAdapter);
            Utils.setListViewHeightBasedOnChildren(binding.citationList);

        }

    }

    @Override
    public void bioUpdated(BioResponse.Data bioData) {

        data.setBioText(bioData.getBioText());

        binding.bioEdit.setText("Edit");

        binding.bottomSheetSetBio.bioText.setText("");
        binding.bioText.setText(data.getBioText());

        bioSheetBehaviour.setHideable(true);
        bioSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void imageUploaded(String path) {
        Utils.setImageFromPath(this, binding.profileImage, path, binding.progressBar);
    }

    @Override
    public void message(int message) {
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
    }

    @Override
    public void userDataFetched(UserProfileResponse.Data data) {

        this.data = data;
        Utils.setImageFromPath(this, binding.profileImage, data.getProfileImage(), binding.progressBar);
        binding.username.setText(data.getUsername());
        binding.name.setText("Name : " + data.getName() + " " + data.getLname());
        binding.age.setText("Age : " + data.getAge());
        binding.country.setText(data.getCountryName());

        if (data.getBioText() != null) {
            binding.bioEdit.setText("Edit");
            binding.bioText.setText(data.getBioText());
        } else {
            binding.bioEdit.setText("Add");
        }

        int sum = 0;

        for (int i = 0; i < data.getIntegrity().size(); i++) {
            sum += Integer.parseInt(data.getIntegrity().get(i).getScore());
        }

        binding.integrityScore.setText(sum + " on " + General.getCurrentDateString());

        setWork(data.getWork());

        setEducation(data.getEducation());

        setCitation(data.getCitation());

        addInterest(data.getInterest());

        setIntegrity(data.getIntegrity());


    }

    private void setIntegrity(List<Integrity> integrityList) {

        int sum = 0;

        for (int i = 0; i < integrityList.size(); i++) {
            sum += Integer.parseInt(integrityList.get(i).getScore());
        }

        binding.bottomSheetSetIntegrity.total.setText(String.valueOf(sum));
        binding.bottomSheetSetIntegrity.date.setText(General.getCurrentDateString());
        IntegrityAdapter integrityAdapter = new IntegrityAdapter(integrityList);
        binding.bottomSheetSetIntegrity.integrityList.setAdapter(integrityAdapter);

        binding.integrityScore.setText(sum + " on " + General.getCurrentDateString());

    }

    @SuppressLint("ResourceType")
    private void setWork(List<Work> workList) {
        WorkAdapter workAdapter = null;
        if (workList != null) {

            updatedWorkAdapter = new UpdatedWorkAdapter(this, workList);
            binding.bottomSheetSetWork.workList.setAdapter(updatedWorkAdapter);

            if (workList.size() > 2) {
                workAdapter = new WorkAdapter(workList, 2);
                seeMoreWork = LayoutInflater.from(this).inflate(R.layout.see_more_text_layout, null);
                seeMoreWork.setId(100);
                seeMoreWork.setOnClickListener(this::OnClick);
                binding.workList.addFooterView(seeMoreWork);
                binding.workList.setAdapter(workAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.workList);
            } else {
                workAdapter = new WorkAdapter(workList, workList.size());
                binding.workList.removeFooterView(seeMoreWork);
                binding.workList.setAdapter(workAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.workList);
            }

            if (workList.size() > 0) {
                binding.workEdit.setText("Edit");
            } else {
                binding.workEdit.setText("Add");
            }
        }
    }

    @SuppressLint("ResourceType")
    private void setEducation(List<Education> educationList) {
        EducationAdapter educationAdapter = null;
        if (educationList != null) {

            updatedEducationAdapter = new UpdatedEducationAdapter(this, educationList);
            binding.bottomSheetSetEducation.educationList.setAdapter(updatedEducationAdapter);

            if (educationList.size() > 2) {
                educationAdapter = new EducationAdapter(educationList, 2);

                seeMoreEducation = LayoutInflater.from(this).inflate(R.layout.see_more_text_layout, null);
                seeMoreEducation.setId(200);
                seeMoreEducation.setOnClickListener(this::OnClick);
                binding.educationList.addFooterView(seeMoreEducation);
                binding.educationList.setAdapter(educationAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.educationList);

            } else {

                educationAdapter = new EducationAdapter(educationList, educationList.size());
                binding.educationList.removeFooterView(seeMoreEducation);
                binding.educationList.setAdapter(educationAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.educationList);

            }

            if (educationList.size() > 0) {
                binding.educationEdit.setText("Edit");
            } else {
                binding.educationEdit.setText("Add");

            }
        }
    }

    @SuppressLint("ResourceType")
    private void setCitation(List<Citation> citationList) {
        CitationAdapter citationAdapter = null;
        if (citationList != null) {

            updatedCitationAdapter = new UpdatedCitationAdapter(this, citationList);
            binding.bottomSheetSetCitation.citationList.setAdapter(updatedCitationAdapter);

            if (citationList.size() > 2) {
                citationAdapter = new CitationAdapter(this, citationList, 2);

                seeMoreCitation = LayoutInflater.from(this).inflate(R.layout.see_more_text_layout, null);
                seeMoreCitation.setId(300);
                seeMoreCitation.setOnClickListener(this::OnClick);
                binding.citationList.addFooterView(seeMoreCitation);
                binding.citationList.setAdapter(citationAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.citationList);

            } else {

                citationAdapter = new CitationAdapter(this, citationList, citationList.size());
                binding.citationList.removeFooterView(seeMoreCitation);
                binding.citationList.setAdapter(citationAdapter);
                Utils.setListViewHeightBasedOnChildren(binding.citationList);

            }

            if (citationList.size() > 0) {
                binding.citationEdit.setText("Edit");
            } else {
                binding.citationEdit.setText("Add");

            }
        }
    }

    @SuppressLint("NewApi")
    private void addInterest(List<String> interestList) {

        if (interestList.size() != 0) {
            binding.interestEdit.setText("Edit");
            if ((data.getInterest().size() > 10)) {
                binding.seeMoreLayout.setVisibility(View.VISIBLE);
            } else {
                binding.seeMoreLayout.setVisibility(View.GONE);
            }

            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
            binding.interest.setLayoutManager(layoutManager);
            FlexboxAdapter adapter = new FlexboxAdapter(interestList.size() > 10 ? interestList.subList(0, 10) : interestList.subList(0, interestList.size()), false, 12);
            binding.interest.setAdapter(adapter);

        } else {
            showToast("No interest available");
            binding.interestEdit.setText("Add");
            binding.seeMoreLayout.setVisibility(View.GONE);
        }

    }

    private void addInterestSheet(List<String> interestList) {
        if (interestList.size() != 0) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
            binding.bottomSheetShowInterest.interestRecyclerView.setLayoutManager(layoutManager);
            FlexboxAdapter adapter = new FlexboxAdapter(interestList, false, 12);
            binding.bottomSheetShowInterest.interestRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void workChanged(List<Work> workList, String type) {

        if (type.equalsIgnoreCase(Constants.EDIT)) {
            showToast("Work updated");
            cleanWorkSheet();
        } else if (type.equalsIgnoreCase(Constants.REMOVE)) {
            showToast("Work removed");
        }
        setWork(workList);
    }

    @Override
    public void educationChanged(List<Education> educationList, String type) {
        if (type.equalsIgnoreCase(Constants.EDIT)) {
            showToast("Education updated");
            cleanEducationSheet();
        } else if (type.equalsIgnoreCase(Constants.REMOVE)) {
            showToast("Education removed");
        }
        setEducation(educationList);
    }

    @Override
    public void citationChanged(List<Citation> citationList, String type) {
        if (type.equalsIgnoreCase(Constants.EDIT)) {
            showToast("Citation updated");
            cleanCitationSheet();
        } else if (type.equalsIgnoreCase(Constants.REMOVE)) {
            showToast("Citation removed");
        }
        setCitation(citationList);
    }

    private void cleanWorkSheet() {
        binding.bottomSheetSetWork.jobTitle.setText("");
        binding.bottomSheetSetWork.organisation.setText("");
        binding.bottomSheetSetWork.previous.setChecked(false);
        binding.bottomSheetSetWork.current.setChecked(false);
        binding.bottomSheetSetWork.sendWork.setText("Send");
        binding.bottomSheetSetWork.jobTitle.requestFocus();
    }

    private void cleanEducationSheet() {
        binding.bottomSheetSetEducation.degree.setText("");
        binding.bottomSheetSetEducation.university.setText("");
        binding.bottomSheetSetEducation.institute.setText("");
        binding.bottomSheetSetEducation.sendEducation.setText("Send");
        binding.bottomSheetSetEducation.degree.requestFocus();
    }

    private void cleanCitationSheet() {
        binding.bottomSheetSetCitation.description.setText("");
        binding.bottomSheetSetCitation.link.setText("");
        binding.bottomSheetSetCitation.sendCitation.setText("Send");
        binding.bottomSheetSetCitation.description.requestFocus();
    }

    @Override
    public void deleteWork(int position) {
        pos = position;
        viewModel.updateDeleteWork(data.getWork().get(position).getId(), Constants.REMOVE, "", "", "");
    }

    @Override
    public void updateWork(int position) {
        pos = position;
        binding.bottomSheetSetWork.sendWork.setText("Update");
        binding.bottomSheetSetWork.jobTitle.setText(data.getWork().get(position).getJobTitle());
        binding.bottomSheetSetWork.organisation.setText(data.getWork().get(position).getOrganisation());
        if (data.getWork().get(position).getIsCurrent() == 0) {
            binding.bottomSheetSetWork.current.setChecked(true);
        } else {
            binding.bottomSheetSetWork.previous.setChecked(true);
        }
        updateWork = true;
    }

    @Override
    public void deleteEducation(int position) {
        pos = position;
        viewModel.updateDeleteEducation(data.getEducation().get(position).getId(), Constants.REMOVE, "", "", "");
    }

    @Override
    public void updateEducation(int position) {
        pos = position;
        binding.bottomSheetSetEducation.sendEducation.setText("Update");
        binding.bottomSheetSetEducation.degree.setText(data.getEducation().get(position).getDegree());
        binding.bottomSheetSetEducation.institute.setText(data.getEducation().get(position).getInstitue());
        binding.bottomSheetSetEducation.university.setText(data.getEducation().get(position).getUniversity());
        updateEducation = true;
    }

    @Override
    public void deleteCitation(int position) {
        pos = position;
        viewModel.updateDeleteCitation(data.getCitation().get(position).getId(), Constants.REMOVE, "", "");
    }

    @Override
    public void updateCitation(int position) {
        pos = position;
        binding.bottomSheetSetCitation.sendCitation.setText("Update");
        binding.bottomSheetSetCitation.description.setText(data.getCitation().get(position).getDescription());
        binding.bottomSheetSetCitation.link.setText(data.getCitation().get(position).getLink());
        updateCitation = true;
    }

    @Override
    public void linkClicked(int position) {
        if (URLUtil.isValidUrl(data.getCitation().get(position).getLink())) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("data", data.getCitation().get(position).getLink());
            startActivity(intent);
        } else {
            showToast("Citation link not valid");
        }
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            pickImageDialog();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        showToast("Error occurred " + error.toString());
                    }
                })
                .check();
    }
}