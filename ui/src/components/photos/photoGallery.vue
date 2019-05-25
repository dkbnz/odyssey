<template>
    <div class="containerWithNav">
        <h1 class="page_title">Personal Media</h1>
        <p class="page_title"><i>Here are your photos</i></p>

        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success"
        >
            <p>Destination Successfully Added</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>

        <b-button class="btn btn-info" block v-b-modal.modalAddPhoto>Add Photo</b-button>
        <b-modal ref="uploaderModal" id="modalAddPhoto" hide-footer centered title="Add Photo">
            <template slot="modal-title"><h2>Add Photo</h2></template>
            <photoUploader v-on:save-photos="sendPhotosToBackend"></photoUploader>
        </b-modal>
        <table style="margin-top:20px">
            <tr v-for="rowNumber in (amountOfRows)">
                <td v-for="photo in getRowPhotos(rowNumber)">
                    <b-container fluid>
                        <b-img :src="getThumbImage(photo)" thumbnail @click="showImage(photo)" alt="Image not Found" @error="imageAlt">
                        </b-img>
                        <b-select v-if="auth" style="width: 210px"
                                  @change="updatePrivacy(photo, profile.photoGallery.find(obj => obj.id === photo).public)"
                                  v-model="profile.photoGallery.find(obj => obj.id === photo).public">
                            <option value="true">
                                Public
                            </option>
                            <option value="false">
                                Private
                            </option>
                        </b-select>
                    </b-container>
                </td>
            </tr>
        </table>
        <b-pagination
                v-model="currentPage"
                :total-rows="rows"
                :per-page="perPage"
        ></b-pagination>
        <b-modal centered hide-footer ref="modalImage">
            <b-img-lazy :src="getFullPhoto()" center fluid></b-img-lazy>
            <b-button  class="mr-2" size="sm"
                       v-b-modal.deletePhotoModal
                       v-if="auth" variant="danger">Delete
            </b-button>
            <b-modal hide-footer id="deletePhotoModal" ref="deletePhotoModal" title="Delete Photo">
                <div class="d-block">
                    Are you sure that you want to delete this image?
                </div>
                <b-button
                        @click="deleteImage();"
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
    import PhotoUploader from "../photos/photoUploader"
    import Assets from '../../assets/index.js'

    export default {
        name: "photoGallery",

        data: function () {
            return {
                photos: [],
                rowSize: 3,
                amountOfRows: 3,
                currentPage: 0,
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
            },
            assets() {
                return Assets
            },
        },

        props: {
            profile: Object,
            userProfile: {
                default: function () {
                    return this.profile
                }
            }
        },

        components: {
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

                }).then(response =>  self.parseJSON(response))
                    .then(data => {
                        this.addPhotos(data);
                    });
                this.$refs['uploaderModal'].hide();
            },


            updatePrivacy(photoId, isPublic) {
                let json = {
                    "id" : photoId,
                    "public" : isPublic
                };
                console.log(photoId);
                console.log(isPublic);

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

            showImage(id) {
                this.currentViewingID = id;
                this.$refs['modalImage'].show();
            },

            getFullPhoto() {
                return 'v1/photos/' + this.currentViewingID;
            },

            getThumbImage(id) {
                return 'v1/photos/thumb/' + id;
            },

            dismissConfirmDelete() {
                this.$refs['deletePhotoModal'].hide();
            },

            deleteImage() {
                fetch(`/v1/photos/` + this.currentViewingID, {
                    method: 'DELETE'
                }).then(response =>  {
                    this.error = (response.status === 200);
                });
                this.$refs['deletePhotoModal'].hide();
                this.$refs['modalImage'].hide();
                this.getPhotos();
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
                this.auth = (this.userProfile.id === this.profile.id || this.userProfile.isAdmin);
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
                        this.photos.push(this.profile.photoGallery[i].id);
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
                        this.photos.push(data[i].id);
                    }
                }
            },

            imageAlt(event) {
                event.target.src = "../../../static/default_profile_picture.png"
            },

            /**
             * Retrieves a json body from a response.
             *
             * @param response      The response parsed into json.
             * @returns {*}
             */
            parseJSON(response) {
                return response.json();
            },

            /**
             * Checks the response status to see if the photos were created by the backend
             */
            checkStatus(response) {
                if (response.status === 201) {
                    self.files = null;
                }
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
                    return this.photos.slice(startRowIndex);
                } else {
                    return this.photos.slice(startRowIndex, endRowIndex);
                }
            }
        }
    }

</script>

<style scoped>

</style>

