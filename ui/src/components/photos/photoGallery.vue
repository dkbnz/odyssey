<template>
    <div class="containerWithNav">
        <h1 class="page_title">Personal Media</h1>
        <p class="page_title"><i>Here are your photos</i></p>
        <b-button class="btn btn-info" block v-b-modal.modalAddPhoto>Add Photo</b-button>
        <b-modal ref="uploaderModal" id="modalAddPhoto" hide-footer centered title="Add Photo">
            <template slot="modal-title"><h2>Add Photo</h2></template>
            <photoUploader v-on:save-photos="sendPhotosToBackend"></photoUploader>
        </b-modal>
        <table style="margin-top:20px">
            <tr v-for="rowNumber in (amountOfRows)">
                <td v-for="photo in getRowPhotos(rowNumber)">
                    <b-img :src="getThumbImage(photo)" thumbnail @click="showImage(photo)" alt="Image not Found">
                    </b-img>
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
                       v-b-modal.deleteModal
                       v-if="auth" v-model="deleteButton" variant="danger">Delete
            </b-button>
            <b-modal hide-footer id="deleteModal" ref="deleteModal" title="Delete Trip">
                <div class="d-block">
                    Are you sure that you want to delete this image?
                </div>
                <b-button
                        @click="dismissModal('deleteModal')
                         deleteImage(selectedTrip);"
                        class="mr-2 float-right"
                        variant="danger">Delete
                </b-button>
                <b-button
                        @click="dismissModal('deleteModal')"
                        class="mr-2 float-right">Cancel
                </b-button>
            </b-modal>
        </b-modal>
    </div>
</template>

<script>
    import PhotoUploader from "../photos/photoUploader"

    export default {
        name: "photoGallery",
        data: function () {
            return {
                photos: [],
                rowSize: 6,
                amountOfRows: 3,
                currentPage: 0,
                currentViewingID: 0,
                auth: false
            }
        },
        computed: {
            rows() {
                return this.photos.length
            },
            perPage() {
                return this.rowSize * this.amountOfRows
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
             * @param files     The photo(s) uploaded from the personal photos component.
             */
            sendPhotosToBackend: function(files) {
                let self = this;
                fetch(`/v1/photos/` + this.profile.id, {
                    method: 'POST',
                    body: this.getFormData(files)

                }).then(response =>  {
                    if (response.status === 201) {
                        self.files = null;
                    } else {
                        console.log("ERROR");
                    }

                });
                this.$refs['uploaderModal'].hide()
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

            /**
             * Creates the form data to send as the body of the POST request to the backend.
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

            checkAuth() {
                this.auth = (this.userProfile.id === this.profile.id || this.userProfile.isAdmin);
            },

            /**
             * TODO: Link to the backend then comment.
             * @returns {Promise<Response | never>}
             */
            getPhotos() {
                this.checkAuth();
                for(let i=0; i < this.profile.photoGallery.length; i++) {
                    console.log(this.profile.photoGallery[i]);
                    if(this.profile.photoGallery[i].public || this.auth) {
                        this.photos.push(this.profile.photoGallery[i].id);
                    }
                }
            },

            /**
             * Retrieves a json body from a response.
             * @param response      The response parsed into json.
             * @returns {*}
             */
            parseJSON(response) {
                return response.json();
            },

            /**
             * Calculates the positions of photos within a gallery grid row.
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

