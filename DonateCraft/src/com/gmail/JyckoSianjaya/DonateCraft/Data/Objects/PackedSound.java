package com.gmail.JyckoSianjaya.DonateCraft.Data.Objects;

import org.bukkit.Sound;

public class PackedSound {
	private float volume;
	private float pitch;
	private Sound sound;
	public PackedSound(Sound sound, float volume, float pitch) {
		this.volume = volume;
		this.sound = sound;
		this.pitch = pitch;
	}
	public Sound getSound() { return this.sound;}
	public float getVolume() { return this.volume; }
	public float getPitch() { return this.pitch; }
	public void setSound(Sound newsound) { this.sound = newsound; }
	public void setVolume(float newvolume) { this.volume = newvolume; }
	public void setPitch(float pitch) { this.pitch = pitch; }
}
