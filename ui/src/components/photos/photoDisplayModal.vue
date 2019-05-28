<template>
    <div>
        <b-modal centered hide-footer ref="modalImage" size="xl">
            <b-img-lazy :src="getFullPhoto()" alt="Image couldn't be retrieved" @error="imageAlt" center fluid>
            </b-img-lazy>
            <b-row>
                <b-col>
                    <b-button
                            :disabled="isProfilePicture"
                            block class="mr-2"
                            size="sm" style="margin-top: 10px"
                            @click="makeProfileImage"
                            v-if="showButtons" variant="info">Make this my profile picture
                    </b-button>
                </b-col>
                <b-col>
                    <b-button
                            block class="mr-2"
                            size="sm" style="margin-top: 10px"
                            v-b-modal.deletePhotoModal
                            v-if="showButtons" variant="danger">Delete
                    </b-button>
                </b-col>
            </b-row>
            <b-modal hide-footer id="deletePhotoModal" ref="deletePhotoModal" title="Delete Photo">
                <div class="d-block">
                    <p>Are you sure that you want to delete this image?</p>
                    <!-- Display additional message if deleting profile picture -->
                    <p v-if="isProfilePicture">
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
    export default {
        name: "photoDisplayModal",
        props: {
            display: Boolean,
            isProfilePicture: Boolean,
            photoToView: Object,
            showButtons: Boolean
        },

        data: function () {
            return {
                auth: false
            }
        },

        mounted() {
            if (this.display) {
                console.log("sdfgdsgf");
                this.$refs['modalImage'].show();
            } else {
                this.$refs['modalImage'].hide();
            }
        },
        methods: {

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
             * Sends the DELETE request to the backend for the selected image and closes the two modals
             * and refreshes the list of photos in the photo gallery.
             */
            deleteImage() {
                let self = this;
                fetch(`/v1/photoToViews/` + this.photo.id, {
                    method: 'DELETE'
                }).then(response => {
                    self.error = (response.status === 200);
            });
                this.$refs['deletePhotoModal'].hide();
                this.$refs['modalImage'].hide();
                this.$emit('delete-photo', this.photoToView);
            },


            /**
             * Emits change up to view profile be able to auto update front end when changing profile photo
             */
            makeProfileImage() {
                this.$refs['modalImage'].hide();
                this.$emit('profile-photo', this.photoToView);
            }
        }
    }
</script>