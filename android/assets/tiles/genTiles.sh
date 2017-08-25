#!/bin/bash
total_tiles=4
total_frames=10
frame_list=

for tilenum in $(eval echo "{1..$total_tiles}")
do
  for frame in $(eval echo "{1..$total_frames}")
  do
    if [ "${frame}" -lt "${total_frames}" ]; then
      frame_list="${frame_list} raw/isotile0${tilenum}-h000${frame}.png"
    else
      frame_list="${frame_list} raw/isotile0${tilenum}-h00${frame}.png"
    fi
  done
done



echo "Packing frames: ${frame_list}"

convert +append ${frame_list} tiles.png
#isotile01-h0000.png
