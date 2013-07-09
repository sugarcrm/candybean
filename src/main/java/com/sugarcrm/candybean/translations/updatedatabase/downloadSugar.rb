#! /usr/bin/env ruby
#
# Candybean is a next generation automation and testing framework suite.
# It is a collection of components that foster test automation, execution
# configuration, data abstraction, results illustration, tag-based execution,
# top-down and bottom-up batches, mobile variants, test translation across
# languages, plain-language testing, and web service testing.
# Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

require 'net/http'
require 'uri'

###############################################################################
# Main Function
#  
# Accepts a git-branch parameter and calls functions in order to download and
# unzip the correct sugar instance
#
###############################################################################
def Main
	puts "starting......"
  jobspace = ARGV[0]
	version = ARGV[1].dup.gsub!("_", ".")
 
	Dir.chdir(jobspace)

	if (/^\d\.\d$/).match(version)
		version = get_newest_version(version)	
		cmd = "wget -N http://10.8.1.16/builds/#{version}/latest/SugarEnt-#{version}.zip 2>&1"
		puts "Running cmd: #{cmd}"
		res = `#{cmd}`
	
		if res.match("Server file no newer than local file")
			puts "latest version already downloaded"
			`rm -Rf sugar`
			puts "extraction in progress...."
			`unzip SugarEnt-#{version}.zip`
			puts "unzipped Sugar!"
	
			Dir.foreach(Dir.pwd) do |child|
				next if !child.match("Sugar")
				next if child.match(/.zip$/)	
				`mv #{child} sugar`
		end
		else
			puts "file downloaded"
			`rm -Rf sugar`
			puts "extraction in progress...."
			`unzip SugarEnt-#{version}.zip`
			puts "unzipped Sugar!"
	
			Dir.foreach(Dir.pwd) do |child|
				next if !child.match("Sugar")
				next if child.match(/.zip$/)	
				`mv #{child} sugar`
		end
	end	

	else
		puts "Error: Cannot find version!"
		exit(1)
	end
end



###############################################################################
# get_newest_version -- function
#     Retrieves newest version of the passed version. i.e. if you pass 6.3.0,
#       it will retrieve 6.3.0RC3 if RC3 is the newest version.
#
# Input: 
#     string: the version we're checking.
#
# Output:
#     string: the newest version we found.
#
###############################################################################
def get_newest_version(version)
  # if the version includes a specific beta or RC, we know they want that specifically.
  if version.match(/beta\d+$/) || version.match(/RC\d+$/)
    puts "(*)Our destination version is: #{version}"
    return version
  end
  
  # retrieving all version matches for this as a base version.
  versions = get_version_matches(version)
  
  # Now find the newest version from all version matches
  time = nil
  newest_version = nil
  versions.each do |ver|
    url = URI.parse("http://10.8.1.16/builds/#{ver}/latest/SugarEnt-#{ver}.zip")
    new_time = nil
    begin  # This is basically saving us if there isn't a latest for this version for whatever reason..
      Net::HTTP.start(url.host) {|http| new_time = Time.parse(http.head(url.path)["last-modified"])}
    rescue # used to have next, but this actually screws us up.
    end
    if !time || (new_time > time)
      time = new_time
      newest_version = ver
    end
  end
  if newest_version.nil?
    puts "(!)Error: No version matches for #{version}"
    exit(1)
  end
  puts "(*)Found newest version of base: #{version} to be: #{newest_version}"
  puts "(*)Our destination version is: #{newest_version}"
  
  newest_version
end

###############################################################################
# get_version_matches -- function
#     Finds all versions that match the base version passed in.  i.e. if you 
#      pass in 6.3.0, it will return all matches including beta1, RC1, etc.
#
# Input: 
#     version - string: the version we're checking.
#     :ga_only - bool: defaults to false.  if we want to find ONLY GA releases
#           i.e. ONLY 6.2.X releases not 6.2.0beta1, 6.2.0RC1, etc.
#     :mask - bool: defaults to false.  if we are getting all versions for a
#           mask set this to true. i.e. if version passed was format "6.2.x" set
#           this to true.
#
# Output:
#     array: the versions we found that include the base version.
#
###############################################################################
def get_version_matches(version)
  
  version.gsub!(".X", '')
  version.gsub!(".x", '')
  version.gsub!(/RC\d/, "RC") # All RC's use the "RC1" upgrade package

  base_url = URI.parse("http://10.8.1.16/builds/") # Honey-b's builds
  retries = 0
  begin
    req = Net::HTTP::Get.new(base_url.path)
    res = Net::HTTP.start(base_url.host, base_url.port) {|http| http.request(req)}
  rescue Timeout::Error => e
    if retries >= 5
      puts "(!)Error: Failed to get_newest_version after #{retries}"
      exit(1)
    end
    retries += 1
    puts "(*)Attempting retry number #{retries}/5 for get_newest_version"
    retry
  end
  versions = []
  res.body.scan(/#{version}.*\">#{version}/).each {|ver| versions << ver.to_s.gsub(/\/\">#{version}/, '')} # finding all the versions (like beta1, or RC1, etc.)
  
  versions
end

Main()
