<template>
    <div class="bg-white m-2 pt-3 pl-3 pr-3 pb-3 rounded-lg">
        <div class="pt-3">
            <h1 class="page-title">Personal Media</h1>
        </div>
        <p v-if="authentication" class="page-title"><i>Here are your photos</i></p>

        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success">
            <p>{{alertMessage}}</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown - 1"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>
        <b-button v-if="authentication" class="btn btn-info" block v-b-modal.modalAddPhoto>Add Photo</b-button>
        <b-modal ref="uploaderModal" id="modalAddPhoto" hide-footer centered title="Add Photo">
            <template slot="modal-title"><h2>Add Photo</h2></template>
            <b-alert dismissible v-model="showError" variant="danger"><p class="wrapWhiteSpace">{{errorMessage}}</p></b-alert>
            <photoUploader v-on:dismiss-error="showError = false"
                           v-on:save-photos="sendPhotosToBackend"
                           :acceptTypes="'image/jpeg, image/jpg, image/png'">
            </photoUploader>
        </b-modal>

        <div class="d-flex justify-content-center mb-3">
            <b-img alt="Loading" class="mt-3 align-middle loading" v-if="retrievingPhotos" :src="assets['loadingLogo']"></b-img>
            <p v-if="photos.length === 0 && !retrievingPhotos"><b>No photos found.</b></p>
        </div>

        <photo-table :photos="photos"
                     :key="reloadPhotoTable"
                     :profile="profile"
                     :userProfile="userProfile"
                     :adminView="adminView"
                     @privacy-update="updatePhotoPrivacy"
                     @photo-click="photoClicked"
        >
        </photo-table>
        <b-modal centered hide-footer ref="modalImage" size="xl">
            <b-img v-if="photoToView !== null" :src="getFullPhoto()" alt="Image couldn't be retrieved"
                        onerror="this.src = '../../../static/default_image.png'" center fluid>
            </b-img>
            <b-row>
                <b-col>
                    <b-button
                            block class="mr-2"
                            size="sm" style="margin-top: 10px"
                            @click="setProfilePhoto"
                            v-if="authentication" variant="info">Make this my profile picture
                    </b-button>
                </b-col>
                <b-col>
                    <b-button
                            block class="mr-2"
                            size="sm" style="margin-top: 10px"
                            v-b-modal.deletePhotoModal
                            v-if="authentication" variant="danger">Delete
                    </b-button>
                </b-col>
            </b-row>
            <b-modal hide-footer id="deletePhotoModal" ref="deletePhotoModal" title="Delete Photo">
                <div class="d-block">
                    <p>Are you sure that you want to delete this image?</p>
                    <p v-if="(photoToView !== null && userProfile.profilePicture !== null
                       && userProfile.profilePicture.id === photoToView.id)">
                        <b>This is your profile picture!</b>
                    </p>
                </div>
                <b-button
                        @click="deleteImage"
                        class="mr-2 float-right"
                        variant="danger">Delete
                </b-button>
                <b-button
                        @click="dismissConfirmDelete"
                        class="mr-2 float-right">Cancel
                </b-button>
            </b-modal>
        </b-modal>

    </div>
</template>

<script>
    import PhotoUploader from "../photos/photoUploader";
    import PhotoTable from "../photos/photoTable";

    export default {
        name: "photoGallery",

        props: {
            profile: Object,
            userProfile: {
                default: function () {
                    return this.profile;
                }
            },
            adminView: Boolean
        },

        watch: {
            profile() {
                this.getPhotosList();
            }
        },

        data: function () {
            return {
                photos: [],
                displayImage: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                alertMessage: "",
                showError: false,
                errorMessage: "",
                photoToView: null,
                refreshTable: 0,
                reloadPhotoTable: 0,
                retrievingPhotos: false,
                refreshPage: 0
            }
        },

        components: {
            PhotoTable,
            PhotoUploader
        },

        computed: {
            authentication() {
                if (this.userProfile.id === undefined) {
                    return true;
                }
                return (this.userProfile.id === this.profile.id
                    || (this.userProfile.admin && this.adminView));
            }
        },

        mounted() {
            let self = this;
            setTimeout(self.getPhotosList, 500);
        },

        methods: {
            /**
             * Creates a POST request to upload photo(s) to the backend.
             *
             * @param files     the photo(s) uploaded from the personal photos component.
             */
            sendPhotosToBackend: function (files) {
                this.showError = false;
                this.errorMessage = "";
                let self = this;
                fetch(`/v1/photos/` + this.profile.id, {
                    method: 'POST',
                    body: this.getFormData(files)

                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response;
                    }
                }).then(function () {
                    self.files = null;
                    self.$refs['uploaderModal'].hide();
                    self.getPhotosList();
                    self.alertMessage = "Photo Successfully Added!";
                    self.showAlert();
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Event handler for selecting a photo modal. Sets the photo to view.
             * Then opens the display modal.
             *
             * @param photo     photo object being clicked on.
             */
            photoClicked(photo) {
                this.photoToView = JSON.parse(JSON.stringify(photo));
                this.$refs['modalImage'].show();
            },


            /**
             * Sends the DELETE request to the backend for the selected image and closes the two modals
             * and refreshes the list of photos in the photo gallery.
             * Deletes the photo from the photos list so it updates the table in the front end without
             * requiring a refresh of the profile.
             */
            deleteImage() {
                let self = this;
                fetch(`/v1/photos/` + this.photoToView.id, {
                    method: 'DELETE'
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response;
                    }
                }).then(function () {
                    self.$refs['deletePhotoModal'].hide();
                    self.$refs['modalImage'].hide();

                    let change = false;
                    for (let i = 0; i < self.photos.length; i++) {
                        if (self.photos[i].id === self.photoToView.id || change) {
                            change = true;
                            if (i + 1 === self.photos.length) {
                                self.photos.pop();
                            } else {
                                self.photos[i] = self.photos[i + 1];
                            }
                        }
                    }
                    self.alertMessage = "Photo Successfully Deleted!";
                    self.showAlert();
                    self.$emit("removePhoto", self.photoToView.id);
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Creates the form data to send as the body of the POST request to the backend.
             *
             * @param files             the photo(s) uploaded from the personal photos component.
             * @returns {FormData}      the FormData stringified for use in the POST request.
             */
            getFormData(files) {
                let personalPhotos = new FormData();
                for (let i = 0; i < files.length; i++) {
                    personalPhotos.append('photo' + i, files[i]);
                }
                return personalPhotos;
            },


            /**
             * Emits change up to view profile be able to auto update front end when changing profile picture.
             */
            setProfilePhoto() {
                let self = this;
                this.photoToView.public = true;
                fetch('/v1/profilePhoto/' + this.photoToView.id, {
                    method: 'PUT'
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response;
                    }
                }).then(function () {
                    self.$refs['modalImage'].hide();
                    self.getPhotosList();
                    self.showError = false;
                    self.$emit('makeProfilePhoto', self.photoToView.id);
                    self.alertMessage = "Profile picture successfully changed!";
                    self.showAlert();
                }).catch(function (response) {
                    console.log("HERE");
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        console.log("HERE");
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Gets a list of the user's photos.
             * Checks for private photos and whether the logged in user has authorisation to view said photos.
             */
            getPhotosList() {
                let self = this;
                this.retrievingPhotos = true;
                fetch(`/v1/photos/user/` + self.profile.id, {
                    accept: "application/json",
                    method: "GET"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.photos = [];
                    for (let i in responseBody) {
                        if (self.authentication || responseBody[i].public || self.adminView) {
                            self.photos.push(responseBody[i]);
                            self.reloadPhotoTable += 1;
                        }
                    }
                    self.showError = false;
                    self.retrievingPhotos = false;
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        self.retrievingPhotos = false;
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Sends a GET request to get the full sized image from the backend.
             */
            getFullPhoto() {
                return 'v1/photos/' + this.photoToView.id;
            },


            /**
             * When an image isn't shown show this default profile image.
             */
            imageAlt(event) {
                event.target.src = "../../../static/default_image.png"
            },


            /**
             * Closes the delete photo modal.
             */
            dismissConfirmDelete() {
                this.$refs['deletePhotoModal'].hide();
            },


            /**
             * Updates the photos list sent to the photoTable for a single privacy photo.
             *
             * @param photo           the photo that's changing status.
             */
            updatePhotoPrivacy(photo) {
                let self = this;
                console.log(photo);
                fetch('/v1/photos', {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(photo)
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.showError = false;
                    self.photos = responseBody;
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        self.retrievingPhotos = false;
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Used to allow an alert to countdown on the successful saving of image/s.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },


            /**
             * Displays the countdown alert on the successful saving of image/s.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            }
        }
    }
</script>
