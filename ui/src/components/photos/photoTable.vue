<template>
    <div>
        <table ref="gallery" style="margin-top:20px">
            <!--Table containing the rows of photos to be displayed-->
            <tr v-for="rowNumber in (amountOfRows)">
                <td v-for="photo in getRowPhotos(rowNumber)">
                    <b-img :src="getThumbImage(photo.id)" @click="showImage(photo.id)" @error="imageAlt"
                           alt="Image not Found" thumbnail>
                    </b-img>
                    <b-select @change="updatePrivacy(photo.id, photo.public)" style="width: 100%"
                              :disabled="userProfile.profilePicture !== null
                               && userProfile.profilePicture.id === photo.id"
                              v-if="auth"
                              v-model="photo.public"
                              :class="{colorBlue: userProfile.profilePicture !== null
                              && userProfile.profilePicture.id !== photo.id,
                              colorDisabled: userProfile.profilePicture !== null
                              && userProfile.profilePicture.id===photo.id}">
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
                :per-page="perPage"
                :total-rows="rows"
                ref="navigationGallery"
                v-model="currentPage"
        ></b-pagination>
    <b-alert v-model="showError" dismissable variant="danger">
        {{alertMessage}}
    </b-alert>
        <b-modal centered hide-footer ref="modalImage" size="xl">
            <b-img-lazy :src="getFullPhoto()" alt="Image couldn't be retrieved" @error="imageAlt" center fluid>
            </b-img-lazy>
            <b-row>
            <b-col>
                <b-button
                        :disabled="this.profile.profilePicture !== null
                           && this.profile.profilePicture.id === this.currentViewingID"
                        block class="mr-2"
                        size="sm" style="margin-top: 10px"
                        @click="makeProfileImage"
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
            photos: Array,
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
                rowSize: 6,
                showError: false,
                alertMessage: ""
            }
        },

        computed: {
            rows() {
                return this.photos.length;
            },
            perPage() {
                return this.rowSize * this.amountOfRows;
            }
        },

        mounted() {
            this.checkAuth()
        },

        methods: {
            /**
             * Calculates the positions of photos within a gallery grid row.
             *
             * @param rowNumber     the row currently having photos positioned within it.
             */
            getRowPhotos(rowNumber) {
                let numberOfPhotos = (this.photos.length);
                let endRowIndex = ((rowNumber * this.rowSize) + ((this.currentPage - 1) * this.perPage));
                let startRowIndex = (rowNumber - 1) * this.rowSize + ((this.currentPage - 1) * this.perPage);

                // Check preventing an IndexOutOfRangeError, before filling the row with photos indexed from the list.
                if (endRowIndex > numberOfPhotos) {
                    return this.photos.slice(startRowIndex);
                } else {
                    return this.photos.slice(startRowIndex, endRowIndex);
                }
            },

            /**
             * Sends a GET request to get the full sized image from the backend.
             */
            getFullPhoto() {
                return 'v1/photos/' + this.currentViewingID;
            },

            /**
             * Sends a GET request to get a thumbnail image from the backend.
             */
            getThumbImage(id) {
                return 'v1/photos/thumb/' + id;
            },

            /**
             * Shows the image in the larger modal and sets the current viewing image.
             */
            showImage(id) {
                this.currentViewingID = id;
                this.$refs['modalImage'].show();
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
             * Sends the DELETE request to the backend for the selected image and closes the two modals
             * and refreshes the list of photos in the photo gallery.
             */
            deleteImage() {
                let self = this;
                fetch(`/v1/photos/` + this.currentViewingID, {
                    method: 'DELETE'
                }).then(response => {
                    self.error = (response.status === 200);
                });
                this.$refs['deletePhotoModal'].hide();
                this.$refs['modalImage'].hide();
                this.$emit('removePhoto', this.currentViewingID);
            },

            /**
             * Emits change up to view profile be able to auto update front end when changing profile picture
             */
            makeProfileImage() {
                let self = this;
                let currentPrivacy = this.photos.find(item => item.id === this.currentViewingID).public;
                this.updatePrivacy(this.currentViewingID, true);
                fetch('/v1/profilePhoto/' + this.currentViewingID, {
                    method: 'PUT'
                }).then(response => {
                    if (response.status === 200) {
                        self.showError = false;
                        this.$emit('makeProfilePhoto', this.currentViewingID);
                    } else {
                        // If the profile picture doesn't update, set back to previous value.
                        self.updatePrivacy(self.currentViewingID, currentPrivacy);
                        self.showError = true;
                        self.alertMessage = "An error occurred when making this your profile photo";
                    }
                });
                this.$refs['modalImage'].hide();
            },

            /**
             * Updates the privacy for a photo between privet and public and sends PATCH
             * request to the backend.
             */
            updatePrivacy(photoId, isPublic) {
                let json = {
                    "id": photoId,
                    "public": isPublic
                };
                let self = this;

                fetch('/v1/photos', {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(json)
                }).then(response => {
                    if (response.status === 200) {
                        self.showError = false;
                        this.$emit('changePrivacy', photoId, isPublic);
                    } else {
                        self.showError = true;
                        self.alertMessage = "Cannot change privacy of your personal photo";
                    }
                });
            },

            /**
             * Checks the authorization of the user profile that is logged in to see if they can.
             * view the users private photos and can add or delete images from the media.
             */
            checkAuth() {
                this.auth = (this.userProfile.id === this.profile.id || (this.userProfile.isAdmin && this.adminView));
            }
        }
    }
</script>

<style scoped>
    .colorBlue {
        color: white;
        font-weight: bold;
        background-color: #85BCE5;
    }

    .colorDisabled {
        background-color: #dddddd;
    }
</style>
