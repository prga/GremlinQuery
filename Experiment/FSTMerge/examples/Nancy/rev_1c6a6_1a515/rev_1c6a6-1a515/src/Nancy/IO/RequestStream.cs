namespace  Nancy.IO {
	
     
     

    /// <summary>
    /// A <see cref="Stream"/> decorator that can handle moving the stream out from memory and on to disk when the contents reaches a certain length.
    /// </summary>
     class  RequestStream   {
		
           

           
           
           
           

        /// <summary>
        /// Initializes a new instance of the <see cref="RequestStream"/> class.
        /// </summary>
        /// <param name="stream">The <see cref="Stream"/> that should be handled by the request stream</param>
        /// <param name="expectedLength">The expected length of the contents in the stream.</param>
        /// <param name="thresholdLength">The content length that will trigger the stream to be moved out of memory.</param>
        /// <param name="disableStreamSwitching">if set to <see langword="true"/> the stream will never explicitly be moved to disk.</param>
          <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770761600/fstmerge_var1_5134377866220562671
RequestStream(Stream stream, long expectedLength, long thresholdLength, bool disableStreamSwitching)
        {
            this.expectedLength = expectedLength;
            this.thresholdLength = thresholdLength;
            this.disableStreamSwitching = disableStreamSwitching;
            this.stream = stream ?? this.CreateDefaultMemoryStream();

            ThrowExceptionIfCtorParametersWereInvalid(this.stream, this.expectedLength, this.thresholdLength);            

            if (this.expectedLength >= this.thresholdLength)
            {
                this.MoveStreamContentsToFileStream();
            } else if (!this.stream.CanSeek) {
                this.MoveStreamContentsToMemoryStream();
            }
            this.stream.Position = 0;
        }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770761600/fstmerge_var2_7599174195104027948
 

        /// <summary>
        /// Gets a value indicating whether the current stream supports reading.
        /// </summary>
        /// <returns>Always returns <see langword="true"/>.</returns>
           

        /// <summary>
        /// Gets a value indicating whether the current stream supports seeking.
        /// </summary>
        /// <returns>Always returns <see langword="true"/>.</returns>
           

        /// <summary>
        /// Gets a value that determines whether the current stream can time out.
        /// </summary>
        /// <returns>Always returns <see langword="false"/>.</returns>
           

        /// <summary>
        /// Gets a value indicating whether the current stream supports writing.
        /// </summary>
        /// <returns>Always returns <see langword="true"/>.</returns>
           

        /// <summary>
        /// Gets the length in bytes of the stream.
        /// </summary>
        /// <returns>A long value representing the length of the stream in bytes.</returns>
           

        /// <summary>
        /// Gets a value indicating whether the current stream is stored in memory.
        /// </summary>
        /// <value><see langword="true"/> if the stream is stored in memory; otherwise, <see langword="false"/>.</value>
        /// <remarks>The stream is moved to disk when either the length of the contents or expected content length exceeds the threshold specified in the constructor.</remarks>
           

        /// <summary>
        /// Gets or sets the position within the current stream.
        /// </summary>
        /// <returns>The current position within the stream.</returns>
           

        /// <summary>
        /// Begins an asynchronous read operation.
        /// </summary>
        /// <returns>An <see cref="T:System.IAsyncResult"/> that represents the asynchronous read, which could still be pending.</returns>
        /// <param name="buffer">The buffer to read the data into. </param>
        /// <param name="offset">The byte offset in <paramref name="buffer"/> at which to begin writing data read from the stream. </param>
        /// <param name="count">The maximum number of bytes to read. </param>
        /// <param name="callback">An optional asynchronous callback, to be called when the read is complete. </param>
        /// <param name="state">A user-provided object that distinguishes this particular asynchronous read request from other requests. </param>
           

        /// <summary>
        /// Begins an asynchronous write operation.
        /// </summary>
        /// <returns>An <see cref="IAsyncResult"/> that represents the asynchronous write, which could still be pending.</returns>
        /// <param name="buffer">The buffer to write data from. </param>
        /// <param name="offset">The byte offset in <paramref name="buffer"/> from which to begin writing. </param>
        /// <param name="count">The maximum number of bytes to write. </param>
        /// <param name="callback">An optional asynchronous callback, to be called when the write is complete. </param>
        /// <param name="state">A user-provided object that distinguishes this particular asynchronous write request from other requests.</param>
           

        /// <summary>
        /// Closes the current stream and releases any resources (such as sockets and file handles) associated with the current stream.
        /// </summary>
           

        /// <summary>
        /// Waits for the pending asynchronous read to complete.
        /// </summary>
        /// <returns>
        /// The number of bytes read from the stream, between zero (0) and the number of bytes you requested. Streams return zero (0) only at the end of the stream, otherwise, they should block until at least one byte is available.
        /// </returns>
        /// <param name="asyncResult">The reference to the pending asynchronous request to finish. </param>
           

        /// <summary>
        /// Ends an asynchronous write operation.
        /// </summary>
        /// <param name="asyncResult">A reference to the outstanding asynchronous I/O request. </param>
           

        /// <summary>
        /// Clears all buffers for this stream and causes any buffered data to be written to the underlying device.
        /// </summary>
           

           

           

           

           

                   

        /// <summary>
        /// Reads a sequence of bytes from the current stream and advances the position within the stream by the number of bytes read.
        /// </summary>
        /// <returns>The total number of bytes read into the buffer. This can be less than the number of bytes requested if that many bytes are not currently available, or zero (0) if the end of the stream has been reached.</returns>
        /// <param name="buffer">An array of bytes. When this method returns, the buffer contains the specified byte array with the values between <paramref name="offset"/> and (<paramref name="offset"/> + <paramref name="count"/> - 1) replaced by the bytes read from the current source. </param>
        /// <param name="offset">The zero-based byte offset in <paramref name="buffer"/> at which to begin storing the data read from the current stream. </param>
        /// <param name="count">The maximum number of bytes to be read from the current stream. </param>
           

        /// <summary>
        /// Reads a byte from the stream and advances the position within the stream by one byte, or returns -1 if at the end of the stream.
        /// </summary>
        /// <returns>The unsigned byte cast to an Int32, or -1 if at the end of the stream.</returns>
           

        /// <summary>
        /// Sets the position within the current stream.
        /// </summary>
        /// <returns>The new position within the current stream.</returns>
        /// <param name="offset">A byte offset relative to the <paramref name="origin"/> parameter. </param>
        /// <param name="origin">A value of type <see cref="T:System.IO.SeekOrigin"/> indicating the reference point used to obtain the new position. </param>
           

        /// <summary>
        /// Sets the length of the current stream.
        /// </summary>
        /// <param name="value">The desired length of the current stream in bytes. </param>
        /// <exception cref="NotSupportedException">The stream does not support having it's length set.</exception>
        /// <remarks>This functionalitry is not supported by the <see cref="RequestStream"/> type and will always throw <see cref="NotSupportedException"/>.</remarks>
           

        /// <summary>
        /// Writes a sequence of bytes to the current stream and advances the current position within this stream by the number of bytes written.
        /// </summary>
        /// <param name="buffer">An array of bytes. This method copies <paramref name="count"/> bytes from <paramref name="buffer"/> to the current stream. </param>
        /// <param name="offset">The zero-based byte offset in <paramref name="buffer"/> at which to begin copying bytes to the current stream. </param>
        /// <param name="count">The number of bytes to be written to the current stream. </param>
          <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770762805/fstmerge_var1_7540691275672430413
void Write(byte[] buffer, int offset, int count)
        {
            this.stream.Write(buffer, offset, count);

            if (this.disableStreamSwitching)
            {
                return;
            }

            if (this.stream.Length >= this.thresholdLength)
            {
                // Close the stream here as closing it every time we call 
                // MoveStreamContentsToFileStream causes an (ObjectDisposedException) 
                // in NancyWcfGenericService - webRequest.UriTemplateMatch
                var old = this.stream;
                this.MoveStreamContentsToFileStream();
                old.Close();                
            }
        }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770762805/fstmerge_var2_8404605786990679299
 

           

           

          <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770762963/fstmerge_var1_1141037407953172120
void MoveStreamContentsToFileStream()
        {            
            MoveStreamContentsInto(CreateTemporaryFileStream());
        }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770762963/fstmerge_var2_5305445679149649379
 

          <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770763017/fstmerge_var1_7119359290308405331
void ThrowExceptionIfCtorParametersWereInvalid(Stream stream, long expectedLength, long thresholdLength)
        {
            if (!stream.CanRead)
            {
                throw new InvalidOperationException("The stream must support reading.");
            }

            if (expectedLength < 0)
            {
                throw new ArgumentOutOfRangeException("expectedLength", expectedLength, "The value of the expectedLength parameter cannot be less than zero.");
            }

            if (thresholdLength < 0)
            {
                throw new ArgumentOutOfRangeException("thresholdLength", thresholdLength, "The value of the threshHoldLength parameter cannot be less than zero.");
            }
        }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390770763017/fstmerge_var2_3174475086813154727
         

        private  void MoveStreamContentsToMemoryStream() {
            MoveStreamContentsInto(new MemoryStream());
        } 

        private  void MoveStreamContentsInto(Stream target) {
            if (this.disableStreamSwitching)
            {
                return;
            }
            if (this.stream.CanSeek && this.stream.Length == 0)
            {
                this.stream.Close();
                this.stream = target;
                return;
            }

            this.stream.CopyTo(target, 8196);
            if (this.stream.CanSeek) 
            {
                this.stream.Flush();
            }
            

            target.Position = 0;
            this.stream = target;
        }
	}

}
