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


    <photo-modal v-bind:photo="{id: 2, public: true}"
                 v-bind:is-profile-picture="false"
                 v-bind:display="true"
                 v-bind:show-buttons="true"
    ></photo-modal>
    </div>
</template>

<script>
    import PhotoUploader from "../photos/photoUploader";
    import PhotoTable from "../photos/photoTable";
    import PhotoModal from "../photos/photoDisplayModal";

    export default {
        name: "photoGallery",
        data: function () {
            return {
                photos: [],
                currentViewingID: 0,
                auth: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                showError: false,
                errorMessage: ""
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
            PhotoUploader,
            PhotoModal
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
            },


            photoClicked: function(photo) {
                console.log(photo);
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
            setProfilePhoto(photoId) {
                this.$emit('makeProfilePhoto', photoId);
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
             * Updates the photos list sent to the photoTable for a single privacy photo.
             *
             * @param photoId           the photo id that's changing status.
             * @param isPublic          the changed status.
             */
            updatePhotoPrivacy: function(photo) {
                let self = this;

                fetch('/v1/photos', {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(photo)
                }).then(response => {
                    if (response.status === 200) {
                        self.photos = response.json();
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
            deletePhoto: function(photoId) {
                this.checkAuth();
                let change = false;
                for(let i=0; i < this.photos.length; i++) {
                    if(this.photos[i].id === photoId || change) {
                        change = true;
                        if (i+1 === this.photos.length) {
                            this.photos.pop();
                        } else {
                            this.photos[i] = this.photos[i+1];
                        }
                    }
                }
                this.$emit("removePhoto", photoId);
            },


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
