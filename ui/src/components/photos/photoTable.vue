<template>
    <div>
    <table style="margin-top:20px" ref="gallery">
        <tr v-for="rowNumber in (amountOfRows)">
            <td v-for="photo in getRowPhotos(rowNumber)">
                <b-img :src="getThumbImage(photo.id)" thumbnail @click="showImage(photo.id)" alt="Image not Found" @error="imageAlt">
                </b-img>
                <b-select v-if="auth" style="width: 100%"
                          @change="updatePrivacy(photo.id, photo.public)"
                          v-model="photo.public">
                    <option value="true">
                        Public
                    </option>
                    <option value="false">
                        Private
                    </option>
                </b-select>
            </td>
        </tr>
    </table>
    <b-pagination
            v-model="currentPage"
            :total-rows="rows"
            :per-page="perPage"
            ref="navigationGallery"
    ></b-pagination>
    <b-modal centered hide-footer size="xl" ref="modalImage">
        <b-img-lazy :src="getFullPhoto()" center fluid></b-img-lazy>
        <b-button  class="mr-2" size="sm"
                   v-b-modal.deletePhotoModal block
                   style="margin-top: 10px"
                   v-if="auth" variant="danger">Delete
        </b-button>
        <b-modal hide-footer id="deletePhotoModal" ref="deletePhotoModal" title="Delete Photo">
            <div class="d-block">
                Are you sure that you want to delete this image?
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
    export default {
        name: "photoTable",
        props: {
            photos: [],
            profile: Object,
            userProfile: {
                default: function () {
                    return this.profile
                }
            },
            adminView: Boolean
        },
        data: function () {
            return {
                amountOfRows: 3,
                currentPage: 1,
                currentViewingID: 0,
                auth: false,
                rowSize: 6
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

        mounted() {
            this.checkAuth()
        },

        methods: {
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
             * Shows the image in the larger modal and sets the current viewing image
             */
            showImage(id) {
                this.currentViewingID = id;
                this.$refs['modalImage'].show();
            },

            /**
             * When an image isn't shown show this default profile image
             */
            imageAlt(event) {
                event.target.src = "../../../static/default_image.png"
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
                this.$emit('removePhoto', this.currentViewingID);
            },

            /**
             * Updates the privacy for a photo between privet and public and sends PATCH
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
                        this.$emit('changePrivacy', photoId, isPublic);
                    } else {
                        console.log("ERROR");
                    }
                });
            },

            /**
             * Checks the authorization of the user profile that is logged in to see if they can
             * view the users private photos and can add or delete images from the media
             */
            checkAuth() {
                this.auth = (this.userProfile.id === this.profile.id || (this.userProfile.isAdmin && this.adminView));
            },
        }
    }
</script>

<style scoped>

</style>