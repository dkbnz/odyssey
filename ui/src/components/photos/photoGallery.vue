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
                rowSize: 3,
                amountOfRows: 3,
                currentPage: 1,
                currentViewingID: 0,
                auth: false,
                dismissSecs: 3,
                dismissCountDown: 0
            }
        },

        computed: {
            rows() {
                return this.photos.length
            },
            perPage() {
                return this.rowSize * this.amountOfRows
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
             * Updates the privacy for a photo between privet and public and sends PACTH
             * request to the backend
             */
            updatePrivacy(photoId, isPublic) {
                let json = {
                    "id" : photoId,
                    "public" : isPublic
                };

                fetch('/v1/photos', {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(json)
                }).then(response => {
                    if (response.status === 200) {
                        self.files = null;
                    } else {
                        console.log("ERROR");
                    }
                });
            },

            /**
             * Shows the image in the larger modal and sets the current viewing image
             */
            showImage(id) {
                this.currentViewingID = id;
                this.$refs['modalImage'].show();
            },

            /**
             * Sends a GET request to get the full sized image from the backend
             */
            getFullPhoto() {
                return 'v1/photos/' + this.currentViewingID;
            },

            /**
             * Sends a GET request to get a thumbnail image from the backend
             */
            getThumbImage(id) {
                return 'v1/photos/thumb/' + id;
            },

            /**
             * Closes the delete photo modal
             */
            dismissConfirmDelete() {
                this.$refs['deletePhotoModal'].hide();
            },

            /**
             * Sends the DELETE request to the backend for the selected image and closes the two modals
             * and refreshes the list of photos in the photo gallery
             */
            deleteImage() {
                fetch(`/v1/photos/` + this.currentViewingID, {
                    method: 'DELETE'
                }).then(response =>  {
                    this.error = (response.status === 200);
                });
                this.$refs['deletePhotoModal'].hide();
                this.$refs['modalImage'].hide();
                this.deletePhoto();
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
             * Calculates the positions of photos within a gallery grid row.
             *
             * @param rowNumber     The row currently having photos positioned within it.
             */
            getRowPhotos(rowNumber) {
                let numberOfPhotos = (this.photos.length);
                let endRowIndex = ((rowNumber * this.rowSize) + ((this.currentPage - 1) * this.perPage));
                let startRowIndex = (rowNumber - 1) * this.rowSize  + ((this.currentPage - 1) * this.perPage);

                // Check preventing an IndexOutOfRangeError, before filling the row with photos indexed from the list.
                if (endRowIndex > numberOfPhotos) {
                    let viewPhotos =  this.photos;
                    return viewPhotos.slice(startRowIndex);
                } else {
                    let viewPhotos =  this.photos;
                    return viewPhotos.slice(startRowIndex, endRowIndex);
                }
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

