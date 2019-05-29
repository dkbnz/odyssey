<template>
    <div class="containerWithNav">
        <h1 class="page-title">Personal Media</h1>
        <p v-if="auth" class="page-title"><i>Here are your photos</i></p>

        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success"
        >
            <p>Photo Successfully Added</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>
        <b-button v-if="auth" class="btn btn-info" block v-b-modal.modalAddPhoto>Add Photo</b-button>
        <b-modal ref="uploaderModal" id="modalAddPhoto" hide-footer centered title="Add Photo">
            <template slot="modal-title"><h2>Add Photo</h2></template>
            <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>
            <photoUploader v-on:dismiss-error="showError = false"
                           v-on:save-photos="sendPhotosToBackend"
                           :acceptTypes="'image/jpeg, image/jpg, image/png'">
            </photoUploader>
        </b-modal>
        <photo-table v-bind:photos="photos"
                     v-bind:profile="profile"
                     v-bind:userProfile="userProfile"
                     :adminView="adminView"
                     v-on:privacy-update="updatePhotoPrivacy"
                     v-on:photo-click="photoClicked"
        >
        </photo-table>

        <b-modal centered hide-footer ref="modalImage" size="xl">
            <b-img-lazy v-if="photoToView !== null" :src="getFullPhoto()" alt="Image couldn't be retrieved" @error="imageAlt" center fluid>
            </b-img-lazy>
            <b-row>
                <b-col>
                    <b-button
                            block class="mr-2"
                            size="sm" style="margin-top: 10px"
                            @click="setProfilePhoto"
                            v-if="auth" variant="info">Make this my profile picture
                    </b-button>
                </b-col>
                <b-col>
                    <b-button
                            block class="mr-2"
                            size="sm" style="margin-top: 10px"
                            v-b-modal.deletePhotoModal
                            v-if="auth" variant="danger">Delete
                    </b-button>
                </b-col>
            </b-row>
            <b-modal hide-footer id="deletePhotoModal" ref="deletePhotoModal" title="Delete Photo">
                <div class="d-block">
                    <p>Are you sure that you want to delete this image?</p>
                    <!-- Display additional message if deleting profile picture -->
                    <p>
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
        data: function () {
            return {
                photos: [],
                displayImage: false,
                auth: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                showError: false,
                errorMessage: "",
                photoToView: null,
                refreshTable: 0,
            }
        },

        props: {
            profile: Object,
            userProfile: {
                default: function () {
                    return this.profile
                }
            },
            adminView: Boolean
        },

        components: {
            PhotoTable,
            PhotoUploader
        },

        mounted() {
            this.getPhotosList()
        },

        methods: {
            /**
             * Creates a POST request to upload photo(s) to the backend.
             *
             * @param files     the photo(s) uploaded from the personal photos component.
             */
            sendPhotosToBackend: function(files) {
                this.showError = false;
                this.errorMessage = "";
                let self = this;
                fetch(`/v1/photos/` + this.profile.id, {
                    method: 'POST',
                    body: this.getFormData(files)

                })  .then(response => self.checkResponse(response))
                    .then(data => {
                        this.addPhotos(data);
                        this.showAlert();
                    });
                this.$emit('photos-saved', true);
            },


            photoClicked: function(photo) {
                this.photoToView = photo;
                this.$refs['modalImage'].show();
            },


            /**
             * Sends the DELETE request to the backend for the selected image and closes the two modals
             * and refreshes the list of photos in the photo gallery.
             */
            deleteImage() {
                let self = this;
                fetch(`/v1/photos/` + this.photoToView.id, {
                    method: 'DELETE'
                }).then(response => {
                    self.error = (response.status === 200);
                });
                this.$refs['deletePhotoModal'].hide();
                this.$refs['modalImage'].hide();

                this.checkAuth();
                let change = false;
                for(let i=0; i < this.photos.length; i++) {
                    if(this.photos[i].id === this.photoToView.id || change) {
                        change = true;
                        if (i+1 === this.photos.length) {
                            this.photos.pop();
                        } else {
                            this.photos[i] = this.photos[i+1];
                        }
                    }
                }
                this.$emit("removePhoto", this.photoToView.id);
            },


            /**
             * Creates the form data to send as the body of the POST request to the backend.
             *
             * @param files             the photo(s) uploaded from the personal photos component.
             * @returns {FormData}      the FormData stringified for use in the POST request.
             */
            getFormData(files) {
                let personalPhotos = new FormData();
                for (let i=0; i < files.length; i++) {
                    personalPhotos.append('photo' + i, files[i]);
                }
                return personalPhotos;
            },


            /**
             * Checks the authorization of the user profile that is logged in to see if they can
             * view the users private photos and can add or delete images from the media.
             */
            checkAuth() {
                this.auth = (this.userProfile.id === this.profile.id || (this.userProfile.isAdmin && this.adminView));
            },


            /**
             * Emits change up to view profile be able to auto update front end when changing profile picture.
             */
            setProfilePhoto() {

                let self = this;
                let currentPrivacy = this.photoToView.public;
                this.photoToView.public = true;
                this.updatePhotoPrivacy(this.photoToView);
                fetch('/v1/profilePhoto/' + this.photoToView.id, {
                    method: 'PUT'
                }).then(response => {
                    if (response.status === 200) {
                        self.refreshTable += 1;
                        self.showError = false;
                        this.$emit('makeProfilePhoto', this.photoToView.id);
                    } else {
                        // If the profile picture doesn't update, set back to previous value.
                        self.photoToView.public = currentPrivacy;
                        self.updatePhotoPrivacy(this.photoToView);
                        self.showError = true;
                        self.alertMessage = "An error occurred when making this your profile photo";
                    }
                });
                this.$refs['modalImage'].hide();
            },


            /**
             * Gets a list of the user's photos.
             * Checks for private photos and whether the logged in user has authorisation to view said photos.
             */
            getPhotosList() {
                let self = this;
                fetch(`/v1/photos/user/` + this.profile.id, {
                    accept: "application/json",
                })
                    .then(response => response.json())
                    .then(photos => {
                        self.checkAuth();
                        for(let i in photos) {
                            if(photos[i].public || this.auth) {
                                self.photos.push(photos[i]);
                            }
                        }
                    })
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
            updatePhotoPrivacy: function(photo) {
                let self = this;

                fetch('/v1/photos', {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(photo)
                }).then(response => {
                    if (response.status === 200) {
                        response.clone().json().then(text => {
                            self.photos = text;
                        });
                    }
                });
            },


            /**
             * When a photo is added it refreshes the photos list without needing a refresh of the page or profile.
             *
             * @param data      the Json response from adding photos from the backend to give id's and public status.
             */
            addPhotos(data) {
                this.checkAuth();
                this.photos = [];
                for(let i=0; i < data.length; i++) {
                    if(data[i].public || this.auth) {
                        this.photos.push(data[i]);
                    }
                }
            },


            /**
             * Deletes the photo from the photos list so it updates the table in the front end without
             * requiring a refresh of the profile.
             */


            /**
             * Retrieves a Json body from a response.
             *
             * @param response      The response parsed into Json.
             * @returns {*}
             */
            checkResponse(response) {
                if (response.status === 201) {
                    this.files = null;
                    this.$refs['uploaderModal'].hide();
                } else {
                    this.showError = true;
                    this.errorMessage = "Invalid image size/type"
                }
                return response.json();

            },

            /**
             * Used to allow an alert to countdown on the successful saving of image/s.
             *JSON
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
