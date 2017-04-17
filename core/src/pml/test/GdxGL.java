package pml.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import org.oscim.backend.GL;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Gerber Lóránt on 2017. 03. 17..
 */

/**
 * LibGDX OpenGL adapter for VTM
 */
public class GdxGL implements GL {
    @Override
    public void attachShader(int program, int shader) {
        Gdx.gl.glAttachShader(program, shader);
    }

    @Override
    public void bindAttribLocation(int program, int index, String name) {
        Gdx.gl.glBindAttribLocation(program, index, name);
    }

    @Override
    public void bindBuffer(int target, int buffer) {
        Gdx.gl.glBindBuffer(target, buffer);
    }

    @Override
    public void bindFramebuffer(int target, int framebuffer) {
        Gdx.gl.glBindFramebuffer(target, framebuffer);
    }

    @Override
    public void bindRenderbuffer(int target, int renderbuffer) {
        Gdx.gl.glBindRenderbuffer(target, renderbuffer);
    }

    @Override
    public void blendColor(float red, float green, float blue, float alpha) {
        Gdx.gl.glBlendColor(red, green, blue, alpha);
    }

    @Override
    public void blendEquation(int mode) {
        Gdx.gl.glBlendEquation(mode);
    }

    @Override
    public void blendEquationSeparate(int modeRGB, int modeAlpha) {
        Gdx.gl.glBlendEquationSeparate(modeRGB, modeAlpha);
    }

    @Override
    public void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
        Gdx.gl.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
    }

    @Override
    public void bufferData(int target, int size, Buffer data, int usage) {
        Gdx.gl.glBufferData(target, size, data, usage);
    }

    @Override
    public void bufferSubData(int target, int offset, int size, Buffer data) {
        Gdx.gl.glBufferSubData(target, offset, size, data);
    }

    @Override
    public int checkFramebufferStatus(int target) {
        return Gdx.gl.glCheckFramebufferStatus(target);
    }

    @Override
    public void compileShader(int shader) {
        Gdx.gl.glCompileShader(shader);
    }

    @Override
    public int createProgram() {
        return Gdx.gl.glCreateProgram();
    }

    @Override
    public int createShader(int type) {
        return Gdx.gl.glCreateShader(type);
    }

    @Override
    public void deleteBuffers(int n, IntBuffer buffers) {
        Gdx.gl.glDeleteBuffers(n, buffers);
    }

    @Override
    public void deleteFramebuffers(int n, IntBuffer framebuffers) {
        Gdx.gl.glDeleteFramebuffers(n, framebuffers);
    }

    @Override
    public void deleteProgram(int program) {
        Gdx.gl.glDeleteProgram(program);
    }

    @Override
    public void deleteRenderbuffers(int n, IntBuffer renderbuffers) {
        Gdx.gl.glDeleteRenderbuffers(n, renderbuffers);
    }

    @Override
    public void deleteShader(int shader) {
        Gdx.gl.glDeleteShader(shader);
    }

    @Override
    public void detachShader(int program, int shader) {
        Gdx.gl.glDetachShader(program, shader);
    }

    @Override
    public void disableVertexAttribArray(int index) {
        Gdx.gl.glDisableVertexAttribArray(index);
    }

    @Override
    public void drawElements(int mode, int count, int type, int offset) {
        Gdx.gl.glDrawElements(mode, count, type, offset);
    }

    @Override
    public void enableVertexAttribArray(int index) {
        Gdx.gl.glEnableVertexAttribArray(index);
    }

    @Override
    public void framebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
        Gdx.gl.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
    }

    @Override
    public void framebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
        Gdx.gl.glFramebufferTexture2D(target, attachment, textarget, texture, level);
    }

    @Override
    public void genBuffers(int n, IntBuffer buffers) {
        Gdx.gl.glGenBuffers(n, buffers);
    }

    @Override
    public void generateMipmap(int target) {
        Gdx.gl.glGenerateMipmap(target);
    }

    @Override
    public void genFramebuffers(int n, IntBuffer framebuffers) {
        Gdx.gl.glGenFramebuffers(n, framebuffers);
    }

    @Override
    public void genRenderbuffers(int n, IntBuffer renderbuffers) {
        Gdx.gl.glGenRenderbuffers(n, renderbuffers);
    }

    @Override
    public String getActiveAttrib(int program, int index, IntBuffer size, Buffer type) {
        return Gdx.gl.glGetActiveAttrib(program, index, size, type);
    }

    @Override
    public String getActiveUniform(int program, int index, IntBuffer size, Buffer type) {
        return Gdx.gl.glGetActiveUniform(program, index, size, type);
    }

    @Override
    public void getAttachedShaders(int program, int maxcount, Buffer count, IntBuffer shaders) {
        Gdx.gl.glGetAttachedShaders(program, maxcount, count, shaders);
    }

    @Override
    public int getAttribLocation(int program, String name) {
        return Gdx.gl.glGetAttribLocation(program, name);
    }

    @Override
    public void getBooleanv(int pname, Buffer params) {
        Gdx.gl.glGetBooleanv(pname, params);
    }

    @Override
    public void getBufferParameteriv(int target, int pname, IntBuffer params) {
        Gdx.gl.glGetBufferParameteriv(target, pname, params);
    }

    @Override
    public void getFloatv(int pname, FloatBuffer params) {
        Gdx.gl.glGetFloatv(pname, params);
    }

    @Override
    public void getFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {
        Gdx.gl.glGetFramebufferAttachmentParameteriv(target, attachment, pname, params);
    }

    @Override
    public void getProgramiv(int program, int pname, IntBuffer params) {
        Gdx.gl.glGetProgramiv(program, pname, params);
    }

    @Override
    public String getProgramInfoLog(int program) {
        return Gdx.gl.glGetProgramInfoLog(program);
    }

    @Override
    public void getRenderbufferParameteriv(int target, int pname, IntBuffer params) {
        Gdx.gl.glGetRenderbufferParameteriv(target, pname, params);
    }

    @Override
    public void getShaderiv(int shader, int pname, IntBuffer params) {
        Gdx.gl.glGetShaderiv(shader, pname, params);
    }

    @Override
    public String getShaderInfoLog(int shader) {
        return Gdx.gl.glGetShaderInfoLog(shader);
    }

    @Override
    public void getShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
        Gdx.gl.glGetShaderPrecisionFormat(shadertype, precisiontype, range, precision);
    }

    @Override
    public void getShaderSource(int shader, int bufsize, Buffer length, String source) {

    }

    @Override
    public void getTexParameterfv(int target, int pname, FloatBuffer params) {
        Gdx.gl.glGetTexParameterfv(target, pname, params);
    }

    @Override
    public void getTexParameteriv(int target, int pname, IntBuffer params) {
        Gdx.gl.glGetTexParameteriv(target, pname, params);
    }

    @Override
    public void getUniformfv(int program, int location, FloatBuffer params) {
        Gdx.gl.glGetUniformfv(program, location, params);
    }

    @Override
    public void getUniformiv(int program, int location, IntBuffer params) {
        Gdx.gl.glGetUniformiv(program, location, params);
    }

    @Override
    public int getUniformLocation(int program, String name) {
        return Gdx.gl.glGetUniformLocation(program, name);
    }

    @Override
    public void getVertexAttribfv(int index, int pname, FloatBuffer params) {
        Gdx.gl.glGetVertexAttribfv(index, pname, params);
    }

    @Override
    public void getVertexAttribiv(int index, int pname, IntBuffer params) {
        Gdx.gl.glGetVertexAttribiv(index, pname, params);
    }

    @Override
    public void getVertexAttribPointerv(int index, int pname, Buffer pointer) {
        Gdx.gl.glGetVertexAttribPointerv(index, pname, pointer);
    }

    @Override
    public boolean isBuffer(int buffer) {
        return Gdx.gl.glIsBuffer(buffer);
    }

    @Override
    public boolean isEnabled(int cap) {
        return Gdx.gl.glIsEnabled(cap);
    }

    @Override
    public boolean isFramebuffer(int framebuffer) {
        return Gdx.gl.glIsFramebuffer(framebuffer);
    }

    @Override
    public boolean isProgram(int program) {
        return Gdx.gl.glIsProgram(program);
    }

    @Override
    public boolean isRenderbuffer(int renderbuffer) {
        return Gdx.gl.glIsRenderbuffer(renderbuffer);
    }

    @Override
    public boolean isShader(int shader) {
        return Gdx.gl.glIsShader(shader);
    }

    @Override
    public boolean isTexture(int texture) {
        return Gdx.gl.glIsTexture(texture);
    }

    @Override
    public void linkProgram(int program) {
        Gdx.gl.glLinkProgram(program);
    }

    @Override
    public void releaseShaderCompiler() {
        Gdx.gl.glReleaseShaderCompiler();
    }

    @Override
    public void renderbufferStorage(int target, int internalformat, int width, int height) {
        Gdx.gl.glRenderbufferStorage(target, internalformat, width, height);
    }

    @Override
    public void sampleCoverage(float value, boolean invert) {
        Gdx.gl.glSampleCoverage(value, invert);
    }

    @Override
    public void shaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
        Gdx.gl.glShaderBinary(n, shaders, binaryformat, binary, length);
    }

    @Override
    public void shaderSource(int shader, String string) {
        Gdx.gl.glShaderSource(shader, string);
    }

    @Override
    public void stencilFuncSeparate(int face, int func, int ref, int mask) {
        Gdx.gl.glStencilFuncSeparate(face, func, ref, mask);
    }

    @Override
    public void stencilMaskSeparate(int face, int mask) {
        Gdx.gl.glStencilMaskSeparate(face, mask);
    }

    @Override
    public void stencilOpSeparate(int face, int fail, int zfail, int zpass) {
        Gdx.gl.glStencilOpSeparate(face, fail, zfail, zpass);
    }

    @Override
    public void texParameterfv(int target, int pname, FloatBuffer params) {
        Gdx.gl.glTexParameterfv(target, pname, params);
    }

    @Override
    public void texParameteri(int target, int pname, int param) {
        Gdx.gl.glTexParameteri(target, pname, param);
    }

    @Override
    public void texParameteriv(int target, int pname, IntBuffer params) {
        Gdx.gl.glTexParameteriv(target, pname, params);
    }

    @Override
    public void uniform1f(int location, float x) {
        Gdx.gl.glUniform1f(location, x);
    }

    @Override
    public void uniform1fv(int location, int count, FloatBuffer v) {
        Gdx.gl.glUniform1fv(location, count, v);
    }

    @Override
    public void uniform1i(int location, int x) {
        Gdx.gl.glUniform1i(location, x);
    }

    @Override
    public void uniform1iv(int location, int count, IntBuffer v) {
        Gdx.gl.glUniform1iv(location, count, v);
    }

    @Override
    public void uniform2f(int location, float x, float y) {
        Gdx.gl.glUniform2f(location, x, y);
    }

    @Override
    public void uniform2fv(int location, int count, FloatBuffer v) {
        Gdx.gl.glUniform2fv(location, count, v);
    }

    @Override
    public void uniform2i(int location, int x, int y) {
        Gdx.gl.glUniform2i(location, x, y);
    }

    @Override
    public void uniform2iv(int location, int count, IntBuffer v) {
        Gdx.gl.glUniform2iv(location, count, v);
    }

    @Override
    public void uniform3f(int location, float x, float y, float z) {
        Gdx.gl.glUniform3f(location, x, y, z);
    }

    @Override
    public void uniform3fv(int location, int count, FloatBuffer v) {
        Gdx.gl.glUniform4fv(location, count, v);
    }

    @Override
    public void uniform3i(int location, int x, int y, int z) {
        Gdx.gl.glUniform3i(location, x, y, z);
    }

    @Override
    public void uniform3iv(int location, int count, IntBuffer v) {
        Gdx.gl.glUniform3iv(location, count, v);
    }

    @Override
    public void uniform4f(int location, float x, float y, float z, float w) {
        Gdx.gl.glUniform4f(location, x, y, z, w);
    }

    @Override
    public void uniform4fv(int location, int count, FloatBuffer v) {
        Gdx.gl.glUniform4fv(location, count, v);
    }

    @Override
    public void uniform4i(int location, int x, int y, int z, int w) {
        Gdx.gl.glUniform4i(location, x, y, z, w);
    }

    @Override
    public void uniform4iv(int location, int count, IntBuffer v) {
        Gdx.gl.glUniform4iv(location, count, v);
    }

    @Override
    public void uniformMatrix2fv(int location, int count, boolean transpose, FloatBuffer value) {
        Gdx.gl.glUniformMatrix2fv(location, count, transpose, value);
    }

    @Override
    public void uniformMatrix3fv(int location, int count, boolean transpose, FloatBuffer value) {
        Gdx.gl.glUniformMatrix3fv(location, count, transpose, value);
    }

    @Override
    public void uniformMatrix4fv(int location, int count, boolean transpose, FloatBuffer value) {
        Gdx.gl.glUniformMatrix4fv(location, count, transpose, value);
    }

    @Override
    public void useProgram(int program) {
        Gdx.gl.glUseProgram(program);
    }

    @Override
    public void validateProgram(int program) {
        Gdx.gl.glValidateProgram(program);
    }

    @Override
    public void vertexAttrib1f(int indx, float x) {
        Gdx.gl.glVertexAttrib1f(indx, x);
    }

    @Override
    public void vertexAttrib1fv(int indx, FloatBuffer values) {
        Gdx.gl.glVertexAttrib1fv(indx, values);
    }

    @Override
    public void vertexAttrib2f(int indx, float x, float y) {
        Gdx.gl.glVertexAttrib2f(indx, x, y);
    }

    @Override
    public void vertexAttrib2fv(int indx, FloatBuffer values) {
        Gdx.gl.glVertexAttrib2fv(indx, values);
    }

    @Override
    public void vertexAttrib3f(int indx, float x, float y, float z) {
        Gdx.gl.glVertexAttrib3f(indx, x, y, z);
    }

    @Override
    public void vertexAttrib3fv(int indx, FloatBuffer values) {
        Gdx.gl.glVertexAttrib3fv(indx, values);
    }

    @Override
    public void vertexAttrib4f(int indx, float x, float y, float z, float w) {
        Gdx.gl.glVertexAttrib4f(indx, x, y, z, w);
    }

    @Override
    public void vertexAttrib4fv(int indx, FloatBuffer values) {
        Gdx.gl.glVertexAttrib4fv(indx, values);
    }

    @Override
    public void vertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
        Gdx.gl.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
    }

    @Override
    public void vertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int offset) {
        Gdx.gl.glVertexAttribPointer(indx, size, type, normalized, stride, offset);
    }

    @Override
    public void activeTexture(int texture) {
        Gdx.gl.glActiveTexture(texture);
    }

    @Override
    public void bindTexture(int target, int texture) {
        Gdx.gl.glBindTexture(target, texture);
    }

    @Override
    public void blendFunc(int sfactor, int dfactor) {
        Gdx.gl.glBlendFunc(sfactor, dfactor);
    }

    @Override
    public void clear(int mask) {
        Gdx.gl.glClear(mask);
    }

    @Override
    public void clearColor(float red, float green, float blue, float alpha) {
        Gdx.gl.glClearColor(red, green, blue, alpha);
    }

    @Override
    public void clearDepthf(float depth) {
        Gdx.gl.glClearDepthf(depth);
    }

    @Override
    public void clearStencil(int s) {
        Gdx.gl.glClearStencil(s);
    }

    @Override
    public void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        Gdx.gl.glColorMask(red, green, blue, alpha);
    }

    @Override
    public void compressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
        Gdx.gl.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
    }

    @Override
    public void compressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
        Gdx.gl.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
    }

    @Override
    public void copyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
        Gdx.gl.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
    }

    @Override
    public void copyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
        Gdx.gl.glCopyTexImage2D(target, level, xoffset, yoffset, x, y, width, height);
    }

    @Override
    public void cullFace(int mode) {
        Gdx.gl.glCullFace(mode);
    }

    @Override
    public void deleteTextures(int n, IntBuffer textures) {
        Gdx.gl.glDeleteTextures(n, textures);
    }

    @Override
    public void depthFunc(int func) {
        Gdx.gl.glDepthFunc(func);
    }

    @Override
    public void depthMask(boolean flag) {
        Gdx.gl.glDepthMask(flag);
    }

    @Override
    public void depthRangef(float zNear, float zFar) {
        Gdx.gl.glDepthRangef(zNear, zFar);
    }

    @Override
    public void disable(int cap) {
        Gdx.gl.glDisable(cap);
    }

    @Override
    public void drawArrays(int mode, int first, int count) {
        Gdx.gl.glDrawArrays(mode, first, count);
    }

    @Override
    public void drawElements(int mode, int count, int type, Buffer indices) {
        Gdx.gl.glDrawElements(mode, count, type, indices);
    }

    @Override
    public void enable(int cap) {
        Gdx.gl.glEnable(cap);
    }

    @Override
    public void finish() {
        Gdx.gl.glFinish();
    }

    @Override
    public void flush() {
        Gdx.gl.glFlush();
    }

    @Override
    public void frontFace(int mode) {
        Gdx.gl.glFrontFace(mode);
    }

    @Override
    public void genTextures(int n, IntBuffer textures) {
        Gdx.gl.glGenTextures(n, textures);
    }

    @Override
    public int getError() {
        return Gdx.gl.glGetError();
    }

    @Override
    public void getIntegerv(int pname, IntBuffer params) {
        Gdx.gl.glGetIntegerv(pname, params);
    }

    @Override
    public String getString(int name) {
        return Gdx.gl.glGetString(name);
    }

    @Override
    public void hint(int target, int mode) {
        Gdx.gl.glHint(target, mode);
    }

    @Override
    public void lineWidth(float width) {
        Gdx.gl.glLineWidth(width);
    }

    @Override
    public void pixelStorei(int pname, int param) {
        Gdx.gl.glPixelStorei(pname, param);
    }

    @Override
    public void polygonOffset(float factor, float units) {
        Gdx.gl.glPolygonOffset(factor, units);
    }

    @Override
    public void readPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
        Gdx.gl.glReadPixels(x, y, width, height, format, type, pixels);
    }

    @Override
    public void scissor(int x, int y, int width, int height) {
        Gdx.gl.glScissor(x, y, width, height);
    }

    @Override
    public void stencilFunc(int func, int ref, int mask) {
        Gdx.gl.glStencilFunc(func, ref, mask);
    }

    @Override
    public void stencilMask(int mask) {
        Gdx.gl.glStencilMask(mask);
    }

    @Override
    public void stencilOp(int fail, int zfail, int zpass) {
        Gdx.gl.glStencilOp(fail, zfail, zpass);
    }

    @Override
    public void texImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
        Gdx.gl.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }

    @Override
    public void texParameterf(int target, int pname, float param) {
        Gdx.gl.glTexParameterf(target, pname, param);
    }

    @Override
    public void texSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
        Gdx.gl.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
    }

    @Override
    public void viewport(int x, int y, int width, int height) {
        Gdx.gl.glViewport(x, y, width, height);
    }
}
