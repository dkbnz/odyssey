<template>
    <div class="containerWithNav">
        <h1 class="page_title">Personal Media</h1>
        <p v-if="auth" class="page_title"><i>Here are your photos</i></p>

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
            <photoUploader v-on:save-photos="sendPhotosToBackend" :acceptTypes="'image/jpeg, image/jpg, image/png'"></photoUploader>
        </b-modal>
        <photo-table v-bind:photos="photos" v-bind:profile="profile" v-bind:userProfile="userProfile" :adminView="adminView" v-on:changePrivacy="updatePhotoPrivacyList"></photo-table>
    </div>
</template>

<script>
    import PhotoUploader from "../photos/photoUploader"
    import PhotoTable from "./photoTable";

    export default {
        name: "photoGallery",

        data: function () {
            return {
                photos: [],
                currentViewingID: 0,
                auth: false,
                dismissSecs: 3,
                dismissCountDown: 0
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
            this.getPhotos()
        },

        methods: {

            /**
             * Creates a POST request to upload photo(s) to the backend.
             *
             * @param files     The photo(s) uploaded from the personal photos component.
             */
            sendPhotosToBackend: function(files) {
                let self = this;
                fetch(`/v1/photos/` + this.profile.id, {
                    method: 'POST',
                    body: this.getFormData(files)

                })  .then(response =>  self.parseJSON(response))
                    .then(data => {
                        this.addPhotos(data);
                        this.showAlert();
                    });
                this.$refs['uploaderModal'].hide();
            },


            /**
             * Creates the form data to send as the body of the POST request to the backend.
             *
             * @param files             The photo(s) uploaded from the personal photos component.
             * @returns {FormData}      The FormData stringified for use in the POST request.
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
             * view the users private photos and can add or delete images from the media
             */
            checkAuth() {
                this.auth = (this.userProfile.id === this.profile.id || (this.userProfile.isAdmin && this.adminView));
            },

            /**
             * Calls the authorization of the user checks for private photos then adds
             * to the photos list all the viewable images the user profile can view
             *
             * @returns {Promise<Response | never>}
             */
            getPhotos() {
                this.checkAuth();
                for(let i=0; i < this.profile.photoGallery.length; i++) {
                    if(this.profile.photoGallery[i].public || this.auth) {
                        this.photos.push(this.profile.photoGallery[i]);
                    }
                }
            },

            /**
             * Updates the photos list sent to the photoTable for a single privacy photo
             * @param photoId           the photo id that's changing status
             * @param isPublic          the changed status
             */
            updatePhotoPrivacyList: function(photoId, isPublic) {
                for(let i=0; i < this.photos.length; i++) {
                    if(this.photos[i].id === photoId) {
                        this.photos[i].public = isPublic;
                    }
                }
            },

            /**
             * When a photo is added it only adds the photos to the gallery that are changed
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
             * requiring a refresh of the profile
             */
            deletePhoto() {
                this.checkAuth();
                for(let i=0; i < this.photos.length; i++) {
                    if(this.photos[i] === this.currentViewingID) {
                        this.photos.splice(i, i-1);
                    }
                }
            },

            /**
             * Retrieves a json body from a response.
             *
             * @param response      The response parsed into json.
             * @returns {*}
             */
            parseJSON(response) {
                if (response.status === 201) {
                    this.files = null;
                }
                return response.json();
            },

            /**
             * Used to allow an alert to countdown on the successful saving of image/s.
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

<style scoped>

</style>

